package io.artoo.ddd.item;

import io.artoo.ddd.domain.Domain;
import io.artoo.ddd.domain.event.History;
import io.artoo.ddd.domain.event.Pending;
import io.artoo.lance.literator.Cursor;

public sealed interface Item extends Pending<Item, Item.Change> {
  enum Lifecycle { OnShelf, Payment, StockOut }

  sealed interface Command extends Domain.Command {
    record Put() implements Command {}
    record Take() implements Command {}
    record Pay() implements Command {}
  }

  sealed interface Change extends Domain.Change {
    record Puted() implements Change {}
    record Taken() implements Change {}
    record Paid() implements Change {}
  }
  static Item create() { return Cursor::nothing; }

  static Item from(History history) {
    return new Snapshot(history);
  }

  @Override
  default Item flush() {
    return Cursor::nothing;
  }
}

final class Aggregate implements Item {

  @Override
  public final Cursor<Change> cursor() {
    return null;
  }
}

final class Snapshot implements Item {
  private final History history;

  Snapshot(final History history) {this.history = history;}

  @Override
  public Cursor<Change> cursor() {
    return history
      .ofType(Change.class)
      .aggregate(
        Item.create(),
        this::match
      )
      .yield()
      .cursor();
  }

  private Item match(Item item, Change change) {
    if (change instanceof Change.Puted) {
      return Lifecycle.OnShelf;
    }
    if (change instanceof Change.Taken) {
      return Lifecycle.InPayment;
    }
    if (change instanceof Change.Paid) {
      return Lifecycle.StockOut;
    }
    throw new IllegalStateException();
  }

}

