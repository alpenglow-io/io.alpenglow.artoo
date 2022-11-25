package io.alpenglow.artoo.lance.func.tail;

import io.alpenglow.artoo.lance.func.TryBiFunction;
import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.func.TryPredicate;
import io.alpenglow.artoo.lance.func.Recursive.Tailrec;

@SuppressWarnings("unchecked")
public final class Aggregate<T, A, R> extends Tailrec<T, A, Aggregate<T, A, R>> {
  private final A aggregated;
  private final TryPredicate<? super T> where;
  private final TryFunction<? super T, ? extends R> select;
  private final TryBiFunction<? super A, ? super R, ? extends A> aggregate;

  private Aggregate(final A aggregated, final TryPredicate<? super T> where, final TryFunction<? super T, ? extends R> select, final TryBiFunction<? super A, ? super R, ? extends A> aggregate) {
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

  public static <T, A, R> Aggregate<T, A, R> with(A seed, TryPredicate<? super T> where, TryFunction<? super T, ? extends R> select, TryBiFunction<? super A, ? super R, ? extends A> aggregate) {
    return new Aggregate<>(seed, where, select, aggregate);
  }

  public static <T, A, R> Aggregate<T, A, R> with(A seed, TryBiFunction<? super A, ? super R, ? extends A> aggregate) {
    return new Aggregate<>(seed, it -> true, it -> (R) it, aggregate);
  }

  public static <T, A, R> Aggregate<T, A, R> with(A seed, TryPredicate<? super T> where, TryBiFunction<? super A, ? super R, ? extends A> aggregate) {
    return new Aggregate<>(seed, it -> true, it -> (R) it, aggregate);
  }
}
