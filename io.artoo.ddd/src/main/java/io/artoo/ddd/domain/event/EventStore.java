package io.artoo.ddd.domain.event;

import io.artoo.ddd.domain.Domain;
import io.artoo.ddd.domain.util.Array;
import io.artoo.ddd.domain.util.Lettering;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;

import java.util.Arrays;

import static io.artoo.ddd.domain.event.EventStore.EventLog;

public interface EventStore extends Many<EventLog> {
  record EventLog(Id id, String eventName, Domain.Change change, Id aggregateId, String aggregateName) {}

  static EventStore create(EventBus eventBus) {
    return new InMemory(eventBus);
  }

  <E extends Domain.Change, P extends Pending<P, E>> P commit(Id id, P pending);
}

final class InMemory implements EventStore, Lettering, Array {
  private enum Sync { Lock }
  private final Sync lock;

  private volatile EventLog[] eventLogs;
  private final EventBus eventBus;

  InMemory(final EventBus eventBus) {
    this(
      Sync.Lock,
      new EventLog[] {},
      eventBus
    );
  }
  private InMemory(final Sync lock, final EventLog[] eventLogs, final EventBus eventBus) {
    this.lock = lock;
    this.eventLogs = eventLogs;
    this.eventBus = eventBus;
  }
  @Override
  public <E extends Domain.Change, P extends Pending<P, E>> P commit(final Id id, final P pending) {
    synchronized (lock) {
      for (final var change : pending) {
        eventLogs = push(this.eventLogs, new EventLog(Id.random(), asKebabCase(change.getClass()), change, id, asKebabCase(pending.getClass())));
        eventBus.emit(change);
      }
    }
    return pending;
  }

  @Override
  public Cursor<EventLog> cursor() {
    synchronized (lock) {
      return Cursor.open(Arrays.copyOf(this.eventLogs, eventLogs.length));
    }
  }
}
