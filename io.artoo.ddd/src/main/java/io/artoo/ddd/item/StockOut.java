package io.artoo.ddd.item;

import io.artoo.ddd.item.Item.Change.Paid;
import io.artoo.ddd.domain.event.Pending;

public interface StockOut extends Pending<Item, Item.Change> {
  default Item pay() {
    return () -> this.concat(new Paid()).cursor();
  }
}
