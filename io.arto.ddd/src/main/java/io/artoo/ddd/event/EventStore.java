package io.artoo.ddd.event;

import io.artoo.ddd.Domain;
import io.artoo.ddd.util.Array;
import io.artoo.ddd.util.Lettering;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

import java.util.UUID;

import static io.artoo.ddd.event.EventStore.EventLog;

public interface EventStore extends Many<EventLog> {
  record EventLog(Id id, String eventName, Domain.Change change, Id aggregateId, String aggregateName) {}

  static EventStore create(EventBus eventBus) {
    return new Synchronized(eventBus);
  }

  <E extends Domain.Change, A extends Changes<E, A>> A commit(A changes);

  <T> History historyOf(Class<T> aggregate, UUID aggregateId);
}

final class Synchronized implements EventStore, Lettering, Array {
  private enum Sync { Lock;}
  private final Sync lock;

  private volatile EventLog[] eventLogs;
  private final EventBus eventBus;
  Synchronized(final EventBus eventBus) {
    this(
      Sync.Lock,
      new EventLog[] {},
      eventBus
    );
  }

  private Synchronized(final Sync lock, final EventLog[] eventLogs, final EventBus eventBus) {
    this.lock = lock;
    this.eventLogs = eventLogs;
    this.eventBus = eventBus;
  }
  @Override
  public <E extends Domain.Event, A extends Changes<E>> A commit(final A aggregate) {
    synchronized (lock) {
      for (final var event : aggregate) {
        eventLogs = push(this.eventLogs, new EventLog(UUID.randomUUID(), asKebabCase(event.getClass()), event, aggregate.uuid(), asKebabCase(aggregate.getClass())));
        eventBus.emit(event);
      }
    }
    return aggregate;
  }

  @Override
  public <E extends Domain.Change, A extends Changes<E, A>> A commit(final A changes) {
    synchronized (lock) {
      for (final var change : changes) {
        eventLogs = push(this.eventLogs, new EventLog(UUID.randomUUID(), asKebabCase(change.getClass()), change, chan.uuid(), asKebabCase(aggregate.getClass())));
        eventBus.emit(change);
      }
    }
  }

  @Override
  public <T> History historyOf(final Class<T> aggregate, final UUID aggregateId) {
    synchronized (lock) {
      return
        History.create(
          this
            .where(eventLog -> eventLog.aggregateName().equals(asKebabCase(aggregate)))
            .where(eventLog -> eventLog.aggregateId().equals(aggregateId))
            .select(EventLog::change)
            .asArrayOf(Domain.Event.class)
        );
    }
  }

  @Override
  public Cursor<EventLog> cursor() {
    return Cursor.open(eventLogs);
  }

}
