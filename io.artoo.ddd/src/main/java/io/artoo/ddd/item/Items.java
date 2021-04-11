package io.artoo.ddd.item;

import io.artoo.ddd.domain.event.EventStore;
import io.artoo.ddd.domain.util.Lettering;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface Items {
  static Items from(EventStore store) {
    return new EventSourced(store);
  }

  Item save(Item item);

  Item findBy(UUID id);

  Item findBy(UUID id, ZonedDateTime at);
}

final class EventSourced implements Items, Lettering {
  private final EventStore eventStore;

  EventSourced(final EventStore eventStore) {
    this.eventStore = eventStore;
  }

  @Override
  public Item save(final Item item) {
    return eventStore.commit(item);
  }

  @Override
  public Item findBy(final UUID id) {
    return Item.from(eventStore.historyOf(Item.class, id));
  }

  @Override
  public Item findBy(final UUID id, final ZonedDateTime at) {
    return null;
  }
}
