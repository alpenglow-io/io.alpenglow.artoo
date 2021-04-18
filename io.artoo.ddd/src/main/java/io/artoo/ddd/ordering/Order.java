package io.artoo.ddd.ordering;

import io.artoo.ddd.domain.Changes;
import io.artoo.ddd.domain.Id;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.One;

import java.time.Instant;

import static io.artoo.ddd.ordering.Order.Status.ApprovalPending;
import static io.artoo.ddd.ordering.Order.Status.Approved;
import static io.artoo.ddd.ordering.Ordering.*;

public interface Order extends One<Order.State> {
  enum Status { ApprovalPending, Approved, Rejected, RevisionPending, CancelPending, Canceled, Done}

  record State(Status status) {
  }

  static Order make(Id id) {
    return new Initial(id);
  }

  default Order approve(Instant actual) {
    return () -> this
      .where(state -> state.status == ApprovalPending)
      .selection(state -> new InProgress(this, Changes.append(Made.event(actual)), Approved))
      .cursor();
  }

  default Order revise(Instant actual) {
    return () -> this
      .where(state -> state.status == Approved)
      .selection(state -> new InProgress(this, Changes.append(), status))
  }

  final class Initial implements Order {
    private final Id id;

    private Initial(final Id id) {
      this.id = id;
    }

    @Override
    public Cursor<State> cursor() {
      return Cursor.open(new State(ApprovalPending));
    }
  }

  final class InProgress implements Order {
    private final Order order;
    private final Changes changes;
    private final Status status;

    public InProgress(final Order order, final Changes changes, final Status status) {
      this.order = order;
      this.changes = changes;
      this.status = status;
    }

    @Override
    public Cursor<State> cursor() {
      return Cursor.open(new State(status));
    }
  }

  final class Final implements Order {
    private final Status status;
    private final Changes changes;

    public Final(final Status status, final Changes changes) {
      this.status = status;
      this.changes = changes;
    }

    @Override
    public Cursor<State> cursor() {
      return changes
        .last()
        .where(event -> event instanceof Made && status == Status.Done)
        .select(event -> Changes.append(event))
        .cursor();
    }
  }
}
