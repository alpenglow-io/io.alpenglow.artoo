package io.artoo.ddd.ordering;

import io.artoo.ddd.domain.Domain.Pending;
import io.artoo.ddd.domain.Domain.Aggregate;
import io.artoo.ddd.domain.History;
import io.artoo.lance.query.One;

import java.time.Instant;

import static io.artoo.ddd.ordering.Order.Status.ApprovalPending;
import static io.artoo.ddd.ordering.Order.Status.Approved;
import static io.artoo.ddd.ordering.Order.Status.Disapproved;
import static io.artoo.ddd.ordering.Order.Status.RevisionPending;
import static io.artoo.ddd.ordering.Ordering.Approved;
import static io.artoo.ddd.ordering.Ordering.ReviseCancelled;
import static io.artoo.ddd.ordering.Ordering.ReviseConfirmed;
import static io.artoo.ddd.ordering.Ordering.Revised;

public interface Order extends Aggregate<Order.State> {
  enum Status {ApprovalPending, Approved, Disapproved, Rejected, RevisionPending, CancelPending, Canceled, Done}

  record State(Status status) {}

  static Order from(History history) {
    return history
      .aggregate(
        Order.create(),
        (order, source) -> {
          if (source.event() instanceof Approved approved) return order.approve(Instant.now());
          if (source.event() instanceof Revised revised) return order.revise(Instant.now());

          throw new IllegalStateException();
        }
      )
      .yield();
  }

  static Order create() {
    return () -> One
      .from(new State(ApprovalPending))
      .select(Pending::state)
      .cursor();
  }

  default Order approve(Instant actual) {
    return () -> this
      .where(aggregate -> aggregate.state().status == ApprovalPending)
      .select(aggregate -> aggregate.change(new State(Approved), Ordering.Approved.event(actual)))
      .cursor();
  }

  default Order revise(Instant actual) {
    return () -> this
      .where(aggregate -> aggregate.state().status == Approved)
      .select(aggregate -> aggregate.change(new State(RevisionPending), Revised.event(actual)))
      .cursor();
  }

  default Order confirmRevise(Instant actual) {
    return () -> this
      .where(aggregate -> aggregate.state().status == RevisionPending)
      .select(aggregate -> aggregate.change(new State(Approved), ReviseConfirmed.event(actual)))
      .cursor();
  }

  default Order cancelRevise() {
    return () -> this
      .where(aggregate -> aggregate.state().status == RevisionPending)
      .select(aggregate -> aggregate.change(new State(Disapproved), ReviseCancelled.event(Instant.now())))
      .cursor();
  }
}
