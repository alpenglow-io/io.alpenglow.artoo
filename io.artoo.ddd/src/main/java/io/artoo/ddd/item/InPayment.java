package io.artoo.ddd.item;

import io.artoo.ddd.item.Item.Change.Taken;
import io.artoo.ddd.domain.event.Pending;
import io.artoo.lance.literator.Cursor;

public sealed interface InPayment extends Pending<Item, Item.Change> {
  default Item take() {
    return new Item() {
      @Override
      public Cursor<Change> cursor() {
        return InPayment.this.concat(new Taken()).cursor();
      }
    };
  }

  final class Take implements Item {
    private final Item item;

    private Take(final Item item) {
      this.item = item;
    }

    @Override
    public Cursor<Change> cursor() {
      return item instanceof Put ;
    }
  }
}
