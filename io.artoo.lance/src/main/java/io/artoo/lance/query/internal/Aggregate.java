package io.artoo.lance.query.internal;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Tail;
import io.artoo.lance.task.Atomic;

public interface Aggregate<T, A, R> extends Func.Uni<T, Tail<T, A, Aggregate<T, A, R>>> {
  static <T, A, R> Atomic<Aggregate<T, A, R>> seeded(
    final A accumulated,
    final Pred.Uni<? super T> where,
    final Func.Uni<? super T, ? extends R> select,
    final Func.Bi<? super A, ? super R, ? extends A> aggregate
  ) {
    return Atomic.reference(new Seeded<>(accumulated, where, select, aggregate));
  }

  final class Seeded<T, A, R> implements Aggregate<T, A, R> {
    private final A accumulated;
    private final Pred.Uni<? super T> where;
    private final Func.Uni<? super T, ? extends R> select;
    private final Func.Bi<? super A, ? super R, ? extends A> aggregate;

    Seeded(final A accumulated, final Pred.Uni<? super T> where, final Func.Uni<? super T, ? extends R> select, final Func.Bi<? super A, ? super R, ? extends A> aggregate) {
      this.accumulated = accumulated;
      this.where = where;
      this.select = select;
      this.aggregate = aggregate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tail<T, A, Aggregate<T, A, R>> tryApply(final T it) throws Throwable {
      if (!where.tryTest(it)) {
        return new Tail<>(this, accumulated);
      } else {
        final var selected = select.tryApply(it);
        final var reduced = accumulated != null ? aggregate.tryApply(accumulated, selected) : (A) selected;
        return
          new Tail<>(
            new Seeded<>(
              reduced,
              where,
              select,
              aggregate
            ),
            reduced
          );
      }
    }
  }
}

