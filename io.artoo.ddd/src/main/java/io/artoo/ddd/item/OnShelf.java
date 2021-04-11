package io.artoo.ddd.item;

import io.artoo.ddd.item.Item.Change.Paid;
import io.artoo.ddd.domain.event.Pending;

public sealed interface OnShelf extends Pending<Item, Item.Change> permits Item {
  default Item put() {
    return () -> this.concat(new Paid()).cursor();
  }
}
