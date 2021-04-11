package io.artoo.ddd.aggregate;

import io.artoo.ddd.event.Changes;
import io.artoo.ddd.util.Array;
import io.artoo.lance.literator.Cursor;

import java.time.Instant;
import java.util.UUID;

public interface Payable extends Changes<Item.Event> {
  default Item pay(UUID uuid, Instant when) {
    return new Pay(this, uuid, when);
  }
}

final class Pay implements Item, Array {
  private final Payable events;
  private final UUID uuid;
  private final Instant when;

  Pay(final Payable events, final UUID uuid, final Instant when) {
    this.events = events;
    this.uuid = uuid;
    this.when = when;
  }

  @Override
  public Cursor<Event> cursor() {
    if (events.all(it -> !(it instanceof Event.Paid)).yield()) {
      return events.concat(new Event.Paid(uuid, when)).cursor();
    }

    throw new IllegalStateException();

  }
}
