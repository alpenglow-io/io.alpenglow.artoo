package io.artoo.lance.func.tail;

import io.artoo.lance.func.Func.MaybeBiFunction;
import io.artoo.lance.func.Func.MaybeFunction;
import io.artoo.lance.func.Pred.MaybePredicate;
import io.artoo.lance.func.TailFunction.Tailrec;

@SuppressWarnings("unchecked")
public final class Aggregate<T, A, R> extends Tailrec<T, A, Aggregate<T, A, R>> {
  private final A aggregated;
  private final MaybePredicate<? super T> where;
  private final MaybeFunction<? super T, ? extends R> select;
  private final MaybeBiFunction<? super A, ? super R, ? extends A> aggregate;

  private Aggregate(final A aggregated, final MaybePredicate<? super T> where, final MaybeFunction<? super T, ? extends R> select, final MaybeBiFunction<? super A, ? super R, ? extends A> aggregate) {
    this.aggregated = aggregated;
    this.where = where;
    this.select = select;
    this.aggregate = aggregate;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Return<T, A, Aggregate<T, A, R>> tryApply(final T it) throws Throwable {
    if (!where.tryTest(it)) {
      return new Return<>(aggregated, this);
    } else {
      final var selected = select.tryApply(it);
      final var newAggregated = aggregated != null ? aggregate.tryApply(aggregated, selected) : (A) selected;
      return Return.with(newAggregated, new Aggregate<>(newAggregated, where, select, aggregate));
    }
  }

  public static <T, A, R> Aggregate<T, A, R> with(A seed, MaybePredicate<? super T> where, MaybeFunction<? super T, ? extends R> select, MaybeBiFunction<? super A, ? super R, ? extends A> aggregate) {
    return new Aggregate<>(seed, where, select, aggregate);
  }

  public static <T, A, R> Aggregate<T, A, R> with(A seed, MaybeBiFunction<? super A, ? super R, ? extends A> aggregate) {
    return new Aggregate<>(seed, it -> true, it -> (R) it, aggregate);
  }

  public static <T, A, R> Aggregate<T, A, R> with(A seed, MaybePredicate<? super T> where, MaybeBiFunction<? super A, ? super R, ? extends A> aggregate) {
    return new Aggregate<>(seed, it -> true, it -> (R) it, aggregate);
  }
}
