package io.artoo.ddd.ordering;

import io.artoo.ddd.domain.EventStore;
import io.artoo.ddd.domain.event.InMemory;
import io.artoo.ddd.domain.util.Lettering;
import io.artoo.ddd.ordering.Order.Event.Approved;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface Orders {
  static Orders from(EventStore store) {
    return new EventSourced(store);
  }

  default Order save(Order order) {
    return InMemory.Store.commit(order);
  }

  Order findBy(UUID id);

  Order findBy(UUID id, ZonedDateTime at);
}

final class EventSourced implements Orders, Lettering {
  private final EventStore eventStore;

  EventSourced(final EventStore eventStore) {
    this.eventStore = eventStore;
  }

  @Override
  public Order save(final Order order) {
    return eventStore.commit(order);
  }

  @Override
  public Order findBy(final UUID id) {
    return Order.from(eventStore.historyOf(Order.class, id));
  }

  @Override
  public Order findBy(final UUID id, final ZonedDateTime at) {
    return null;
  }
}
