package io.artoo.ddd.forum;

import io.artoo.ddd.core.EventStore;
import io.artoo.ddd.core.Id;
import io.artoo.ddd.forum.Ordering.Approved;
import io.artoo.ddd.forum.Ordering.Made;
import io.artoo.lance.query.One;

public interface Orders {
  static Orders from(EventStore store) {
    return new EventSourced(store);
  }

  One<Order> save(Order order);

  Order findBy(Id id);
}

final class EventSourced implements Orders {
  private final EventStore eventStore;

  EventSourced(final EventStore eventStore) {
    this.eventStore = eventStore;
  }

  @Override
  public One<Order> save(final Order order) {
    return eventStore
      .commit(order)
      .where(count -> count > 0)
      .select(it -> order)
      .or("Can't save order", IllegalStateException::new);
  }

  @Override
  public Order findBy(final Id aggregateId) {
    return eventStore
      .findHistoryBy(aggregateId)
      .aggregate(
        Order.create(aggregateId),
        (order, event) -> One.lone(event)
          .when$(Made.class, made -> order)
          .when$(Approved.class, approved -> order.approve(approved.actual()))
          .when$(Ordering.Revised.class, revised -> order.revise(revised.actual()))
          .ofType(Order.class)
          .otherwise("Can't create order aggregate", IllegalStateException::new)
      )
      .otherwise("Can't aggregate events", IllegalStateException::new);
  }
}
