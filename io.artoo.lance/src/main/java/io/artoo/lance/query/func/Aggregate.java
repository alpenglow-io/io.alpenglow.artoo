package io.artoo.lance.query.func;

import io.artoo.lance.func.Func.MaybeBiFunction;
import io.artoo.lance.func.Func.MaybeFunction;
import io.artoo.lance.func.Pred.MaybePredicate;
import io.artoo.lance.query.Tail;
import io.artoo.lance.task.Atomic;

public interface Aggregate<T, A, R> extends MaybeFunction<T, Tail<T, A, Aggregate<T, A, R>>> {
  static <T, A, R> Aggregate<T, A, R> seeded(
    A seed,
    MaybePredicate<? super T> where,
    MaybeFunction<? super T, ? extends R> select,
    MaybeBiFunction<? super A, ? super R, ? extends A> aggregate
  ) {
    return new Seeded<>(seed, where, select, aggregate);
  }

  final class Seeded<T, A, R> implements Aggregate<T, A, R> {
    private final A accumulated;
    private final MaybePredicate<? super T> where;
    private final MaybeFunction<? super T, ? extends R> select;
    private final MaybeBiFunction<? super A, ? super R, ? extends A> aggregate;

    Seeded(final A accumulated, final MaybePredicate<? super T> where, final MaybeFunction<? super T, ? extends R> select, final MaybeBiFunction<? super A, ? super R, ? extends A> aggregate) {
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

