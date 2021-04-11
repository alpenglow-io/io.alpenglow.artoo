package io.artoo.ddd.aggregate;

import io.artoo.ddd.event.Changes;
import io.artoo.lance.literator.Cursor;

public interface Putable extends Changes<Item.Event, Item> {
  default Item put() {
    return new Put(this);
  }
}

final class Put implements Item {
  private final Changes<Event, Item> events;

  Put(final Changes<Event, Item> events) {
    this.events = events;
  }

  @Override
  public Cursor<Event> cursor() {
    return events.concat(new Event.Put()).cursor();
  }
}
