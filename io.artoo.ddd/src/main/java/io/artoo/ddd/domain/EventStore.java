package io.artoo.ddd.domain;

import io.artoo.ddd.domain.event.EventBus;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;
import io.artoo.lance.query.many.Concatenatable;
import io.artoo.lance.query.many.Countable;
import io.artoo.lance.value.Symbol;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public interface EventStore {
  static EventStore inMemory(EventBus eventBus) {
    return new InMemory(eventBus);
  }

  <A extends Domain.Aggregate> One<Integer> commit(A aggregate);

  History findHistoryBy(Id id);

  final class InMemory implements EventStore {
    private record EventLog(
      Symbol eventId,
      String eventName,
      Domain.Event eventData,
      Id aggregateId,
      String aggregateName,
      Symbol workId,
      Instant persistedAt
    ) {}
    private enum Sync {Lock}

    private final Sync lock;

    private volatile AtomicReference<Many<EventLog>> eventLogs;
    private final EventBus eventBus;

    private InMemory(final EventBus eventBus) {
      this(
        Sync.Lock,
        Many.empty(),
        eventBus
      );
    }

    private InMemory(final Sync lock, final Many<EventLog> eventLogs, final EventBus eventBus) {
      this.lock = lock;
      this.eventLogs = new AtomicReference<>(eventLogs);
      this.eventBus = eventBus;
    }

    @Override
    public <A extends Domain.Aggregate> One<Integer> commit(final A aggregate) {
      synchronized (lock) {
        return aggregate
          .select(work ->
            eventLogs
              .get()
              .any(log -> log.workId.is(work.id()))
              .where(it -> !it)
              .selection(it ->
                work
                  .changes()
                  .select(event ->
                    new EventLog(
                      Symbol.unique(),
                      event.$name(),
                      event,
                      work.aggregateId(),
                      aggregate.$name(),
                      work.id(),
                      Instant.now()
                    )
                  )
              )
          )
          .peek(logs -> eventLogs
            .accumulateAndGet(logs, Concatenatable::concat)
            .eventually(log -> eventBus.emit(new Domain.EventMessage(log.eventId, log.eventData, log.aggregateId, log.persistedAt, Instant.now())))
          )
          .select(logs -> logs.count())
          .otherwise("Can't commit aggregate", IllegalStateException::new);
      }
    }

    @Override
    public History findHistoryBy(final Id aggregateId) {
      synchronized (lock) {
        return
          History.past(
            eventLogs
              .get()
              .where(log -> log.aggregateId.is(aggregateId))
              .select(log -> log.eventData)
              .asArrayOf(Domain.Event.class)
          );
      }
    }
  }
}

