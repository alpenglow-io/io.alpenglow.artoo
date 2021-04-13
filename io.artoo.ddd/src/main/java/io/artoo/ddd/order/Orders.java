package io.artoo.ddd.order;

import io.artoo.ddd.domain.event.EventStore;
import io.artoo.ddd.domain.util.Lettering;
import io.artoo.ddd.order.Order.Event.Approved;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface Orders {
  static Orders from(EventStore store) {
    return new EventSourced(store);
  }

  Order save(Order order);

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
    return eventStore.commit(
      order,
      it -> it
        .ofType(Approved.class)
        .select(Approved::id)
        .single()
        .yield()
    );
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
