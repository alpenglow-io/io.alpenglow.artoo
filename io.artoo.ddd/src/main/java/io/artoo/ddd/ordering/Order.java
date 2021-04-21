package io.artoo.ddd.ordering;

import io.artoo.ddd.domain.Domain;
import io.artoo.ddd.domain.Domain.Aggregate;
import io.artoo.ddd.domain.Domain.UnitOfWork;
import io.artoo.ddd.domain.History;
import io.artoo.ddd.domain.Id;
import io.artoo.ddd.ordering.order.Changeable;
import io.artoo.lance.literator.Cursor;

import java.time.Instant;

import static io.artoo.ddd.ordering.Order.Status.ApprovalPending;
import static io.artoo.ddd.ordering.Order.Status.Approved;
import static io.artoo.ddd.ordering.Order.Status.Disapproved;
import static io.artoo.ddd.ordering.Order.Status.RevisionPending;
import static io.artoo.ddd.ordering.Ordering.Approved;
import static io.artoo.ddd.ordering.Ordering.ReviseCancelled;
import static io.artoo.ddd.ordering.Ordering.ReviseConfirmed;
import static io.artoo.ddd.ordering.Ordering.Revised;

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
