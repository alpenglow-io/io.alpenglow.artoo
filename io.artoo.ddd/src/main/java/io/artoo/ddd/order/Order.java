package io.artoo.ddd.order;

import io.artoo.ddd.domain.Domain;
import io.artoo.ddd.domain.event.EventStore;
import io.artoo.ddd.domain.event.Id;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.tuple.Single;
import io.artoo.lance.tuple.Tuple;

import static io.artoo.ddd.order.Order.Lifecycle.ApprovalPending;

public sealed interface Order extends Single<Id> {

  default String $name() { return "order"; }

  enum Lifecycle {ApprovalPending, Approved, RevisionPending, CancelPending, Cancelled, Rejected, Done}

  sealed interface Command extends Domain.Command {
    record Put() implements Command {}

    record Take() implements Command {}

    record Pay() implements Command {}
  }

  sealed interface Event extends Domain.Event {
    record Created(Id id) implements Event {}

    record Approved() implements Event {}

    record Revised() implements Event {}
  }

  static Order create(Id id) { return new ApprovalPending(id); }

  static Order from(Id id, EventStore store) {
    return new io.artoo.ddd.order.Snapshot(id, store);
  }

  default Order approve() { return new Approved(this); }

  default Order revise() { return new RevisionPending(this); }
}

record Plain(Id id) implements Order {

}

final class ApprovalPending implements Order {
  private final Id id;

  ApprovalPending(final Id id) {this.id = id;}

  @Override
  public Cursor<Event> cursor() {
    return Cursor.open(new Order.Event.Created(id));
  }
}

final class Approved implements Order {
  private final Order order;

  Approved(final Order order) {this.order = order;}

  @Override
  public Cursor<Event> cursor() {
    return
      (
        order instanceof ApprovalPending
          ? order.concat(new Event.Approved())
          : One.<Event>gone("Order can't be authorized", IllegalStateException::new)
      ).cursor();
  }
}

final class RevisionPending implements Order {
  private final Order order;

  RevisionPending(final Order order) {this.order = order;}

  @Override
  public Cursor<Event> cursor() {
    return
      (
        order instanceof Approved
          ? order.concat(new Event.Revised())
          : One.<Event>gone("Order can't be revised", IllegalStateException::new)
      ).cursor();
  }
}

final class InProgress implements Order {
  private final Order order;
  private final Lifecycle lifecycle;

  InProgress(final Order order, final Lifecycle lifecycle) {
    this.order = order;
    this.lifecycle = lifecycle;
  }

  @Override
  public Cursor<Event> cursor() {
    return switch (lifecycle) {
      case ApprovalPending -> order.concat(new Event.Approved(Id.random())).cursor();
      case Approved ->
      default -> throw new IllegalStateException("Unstable state");
    };
  }
}

final class Uncommitted implements Order {
  private final Lifecycle lifecycle;
  private final Order.Event[] events;

  Uncommitted(final Event... events) { this(ApprovalPending, events); }

  Uncommitted(final Lifecycle lifecycle, final Event... events) {
    this.events = events;
    this.lifecycle = lifecycle;
  }

  @Override
  public final Cursor<Event> cursor() {
    return switch (lifecycle) {
      case ApprovalPending -> new Uncommitted(Lifecycle.Approved, new Event.Approved();
      default -> concat(new Event.)
    })
      .
    cursor()
  }
}

final class Snapshot implements Order {
  private final Id id;
  private final EventStore eventLogs;

  Snapshot(final Id id, final EventStore eventLogs) {
    this.id = id;
    this.eventLogs = eventLogs;
  }

  @Override
  public Cursor<Event> cursor() {
    return eventLogs
      .where(eventLog -> eventLog.aggregateId().equals(id))
      .ofType(Event.class)
      .aggregate(Order.create(), this::match)
      .yield()
      .cursor();
  }

  private Order match(Order order, Event change) {
    if (change instanceof Event.Puted) {
      return ApprovalPending;
    }
    if (change instanceof Event.Taken) {
      return Lifecycle.InPayment;
    }
    if (change instanceof Event.Paid) {
      return Lifecycle.StockOut;
    }
    throw new IllegalStateException();
  }

}

