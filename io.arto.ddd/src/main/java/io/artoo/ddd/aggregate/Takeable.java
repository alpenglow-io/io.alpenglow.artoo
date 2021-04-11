package io.artoo.ddd.aggregate;

import io.artoo.ddd.event.Changes;
import io.artoo.ddd.util.Array;
import io.artoo.lance.literator.Cursor;

import java.time.Instant;
import java.util.UUID;

public interface Takeable extends Changes<Item.Event, Item> {
  default Item take(UUID uuid, Instant when) {
    return () -> concat(new Item.Event.Taken(uuid, when)).cursor();
  }
}

final class Take implements Item, Array {
  private final Changes<Event> events;
  private final Instant when;
  private final UUID uuid;

  Take(final Changes<Event> events, final UUID uuid, final Instant when) {
    this.events = events;
    this.uuid = uuid;
    this.when = when;
  }

  private boolean isNotPaid() {
    return events
      .all(it -> !(it instanceof Event.Paid))
      .yield();
  }

  private boolean isStocked() {
    return events.any(Event.Put.class::isInstance).yield();
  }

  @Override
  public Cursor<Event> cursor() {
    if (isStocked() && isNotPaid()) {
      return events.concat(new Event.Taken(uuid, when)).cursor();
    }

    throw new IllegalStateException();
  }
}
