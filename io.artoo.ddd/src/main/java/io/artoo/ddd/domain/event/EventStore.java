package io.artoo.ddd.domain.event;

import io.artoo.ddd.domain.Domain;
import io.artoo.ddd.domain.Domain.Event.Log;
import io.artoo.ddd.domain.Id;
import io.artoo.ddd.domain.util.Array;
import io.artoo.lance.func.Func;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

import java.time.Instant;

public interface EventStore extends Many<Log> {
  static EventStore create(EventBus eventBus) {
    return new InMemory(eventBus);
  }

  <E extends Domain.Event, U extends UnitOfWork<E>> Many<Log> commit(U unitOfWork, Func.Uni<? super U, ? extends Id> id);

  final class InMemory implements EventStore {
    private static final class Logs implements Array {
      private Log[] logs;

      private Logs(Log... logs) {
        this.logs = logs;
      }

      private void push(Log log) {
        logs = push(logs, log);
      }

      private Log[] copy() {
        return copy(logs);
      }
    }

    private enum Sync {Lock}

    private final Sync lock;
    private volatile Logs logs;

    private final EventBus eventBus;

    private InMemory(final EventBus eventBus) {
      this(
        Sync.Lock,
        new Log[]{},
        eventBus
      );
    }

    private InMemory(final Sync lock, final Log[] logs, final EventBus eventBus) {
      this.lock = lock;
      this.logs = new Logs(logs);
      this.eventBus = eventBus;
    }

    @Override
    public <E extends Domain.Event, U extends UnitOfWork<E>> Many<Log> commit(final U unitOfWork, final Func.Uni<? super U, ? extends Id> id) {
      synchronized (lock) {
        return
          Many.from(id.apply(unitOfWork)).selection(aggregateId ->
            unitOfWork
              .select(event -> new Log(Id.random(), event.$name(), event, aggregateId, unitOfWork.$name(), Instant.now()))
              .peek(log -> logs.push(log))
              .peek(eventBus::emit)
          );
      }
    }

    @Override
    public Cursor<Domain.Event.Log> cursor() {
      synchronized (lock) {
        return Cursor.open(logs.copy());
      }
    }
  }
}

