package io.artoo.ddd.forum;

import io.artoo.ddd.core.Domain;
import io.artoo.ddd.core.Domain.Aggregate;
import io.artoo.ddd.core.Domain.UnitOfWork;
import io.artoo.ddd.core.Id;
import io.artoo.lance.literator.Cursor;

import java.time.Instant;

import static io.artoo.ddd.forum.Order.Status.ApprovalPending;
import static io.artoo.ddd.forum.Order.Status.Approved;
import static io.artoo.ddd.forum.Order.Status.Disapproved;
import static io.artoo.ddd.forum.Order.Status.RevisionPending;
import static io.artoo.ddd.forum.Ordering.Approved;
import static io.artoo.ddd.forum.Ordering.ReviseCancelled;
import static io.artoo.ddd.forum.Ordering.ReviseConfirmed;
import static io.artoo.ddd.forum.Ordering.Revised;

public sealed interface Order extends Changeable, Aggregate {
  enum Status {ApprovalPending, Approved, Disapproved, Rejected, RevisionPending, CancelPending, Canceled, Done}

  static Order from(History history) {
    return history
      .aggregate(
        Order.create(),
        (order, source) -> {
          if (source.event() instanceof Approved)
            return order.approve(Instant.now());
          if (source.event() instanceof Revised)
            return order.revise(Instant.now());

          throw new IllegalStateException();
        }
      )
      .yield();
  }

  static Order create(Id id) {
    return new Aggregate(id, new Aggregate.State(ApprovalPending));
  }

  default Order approve(Instant actual) {
    return () -> this
      .where(work -> work.changes().contains == ApprovalPending)
      .select(aggregate -> aggregate.change(new State(Approved), Ordering.Approved.event(actual)))
      .cursor();
  }

  default Order revise(Instant actual) {
    return () -> this
      .where(aggregate -> aggregate.work().status == Approved)
      .select(aggregate -> aggregate.change(new State(RevisionPending), Revised.event(actual)))
      .cursor();
  }

  default Order confirmRevise(Instant actual) {
    return () -> this
      .where(aggregate -> aggregate.work().status == RevisionPending)
      .select(aggregate -> aggregate.change(new State(Approved), ReviseConfirmed.event(actual)))
      .cursor();
  }

  default Order cancelRevise() {
    return () -> this
      .where(aggregate -> aggregate.work().status == RevisionPending)
      .select(aggregate -> aggregate.change(new State(Disapproved), ReviseCancelled.event(Instant.now())))
      .cursor();
  }

  final class Aggregate implements Order {
    private record State(Status status) {}

    private final Id id;
    private final State state;
    private final Domain.Event[] events;

    private Aggregate(final Id id, final Domain.Event... events) {
      this(id, null, events);
    }

    private Aggregate(final Id id, final Aggregate.State state, final Domain.Event... events) {
      this.id = id;
      this.state = state;
      this.events = events;
    }

    @Override
    public Cursor<UnitOfWork> cursor() {
      return Cursor.nothing();
    }
  }
}
