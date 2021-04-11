package io.artoo.ddd.aggregate;

import io.artoo.ddd.Domain;
import io.artoo.ddd.event.History;
import io.artoo.lance.literator.Cursor;

import java.time.Instant;
import java.util.UUID;

public interface Item extends Putable, Payable, Takeable {
  sealed interface Command extends Domain.Command permits Command.Put, Command.Take, Command.Pay {
    record Put() implements Command {}

    record Take(Instant when) implements Command {}

    record Pay(Instant when) implements Command {}
  }

  sealed interface Event extends Domain.Event permits Event.Paid, Event.Put, Event.Taken {
    record Put(UUID uuid) implements Event {}

    record Taken(UUID uuid, Instant when) implements Event {}

    record Paid(UUID uuid, Instant when) implements Event {}
  }

  static Item create() { return State.Less; }

  static Item from(History history) {
    return new Snapshot(history);
  }

  @Override
  default Item flush() {
    if (this instanceof Put) {
      return new Put(() -> Cursor.nothing());
    }

    throw new IllegalStateException("Should never reach this point");
  }
}

enum State implements Item {
  Less;

  @Override
  public Cursor<Event> cursor() {
    return Cursor.nothing();
  }
}

final class Snapshot implements Item {
  private final History history;

  Snapshot(final History history) {this.history = history;}

  @Override
  public Cursor<Event> cursor() {
    return history
      .ofType(Item.Event.class)
      .aggregate(
        Item.create(),
        this::match
      )
      .yield()
      .cursor();
  }

  private Item match(Item item, Event event) {
    if (event instanceof Event.Put put) {
      return item.put(put.uuid());
    }
    if (event instanceof Event.Taken taken) {
      return item.take(taken.uuid(), taken.when());
    }
    if (event instanceof Event.Paid paid) {
      return item.pay(paid.uuid(), paid.when());
    }
    throw new IllegalStateException();
  }

}

