package io.artoo.ddd.domain;

import io.artoo.ddd.domain.Domain.Event.Log;
import io.artoo.ddd.domain.event.EventBus;
import io.artoo.ddd.domain.util.Array;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

@SuppressWarnings({"Convert2MethodRef", "unchecked"})
public interface EventStore extends Many<EventStore.Log> {
  record Log(Id id, Domain.Event event) {}
  static EventStore create(EventBus eventBus) {
    return new InMemory(eventBus);
  }

  default <A extends Domain.Aggregate<R>, R extends Record> A commit(A aggregate) {
    return (A) (Domain.Aggregate<R>) () -> Cursor.nothing();
  }

  default History findHistoryBy(Id id) {
    return () -> this
      .where(log -> log.id().equals(id))
      .select(log -> new History.Source(log.id(), log.event()))
      .cursor();
  }

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
    public Cursor<Log> cursor() {
      synchronized (lock) {
        return Cursor.open(logs.copy());
      }
    }
  }
}

