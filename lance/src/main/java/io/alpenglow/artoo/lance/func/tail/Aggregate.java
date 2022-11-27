package io.alpenglow.artoo.lance.func.tail;

import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.func.Recursive.Tailrec;

@SuppressWarnings("unchecked")
public final class Aggregate<T, A, R> extends Tailrec<T, A, Aggregate<T, A, R>> {
  private final A aggregated;
  private final TryPredicate1<? super T> where;
  private final TryFunction1<? super T, ? extends R> select;
  private final TryFunction2<? super A, ? super R, ? extends A> aggregate;

  private Aggregate(final A aggregated, final TryPredicate1<? super T> where, final TryFunction1<? super T, ? extends R> select, final TryFunction2<? super A, ? super R, ? extends A> aggregate) {
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

  public static <T, A, R> Aggregate<T, A, R> with(A seed, TryPredicate1<? super T> where, TryFunction1<? super T, ? extends R> select, TryFunction2<? super A, ? super R, ? extends A> aggregate) {
    return new Aggregate<>(seed, where, select, aggregate);
  }

  public static <T, A, R> Aggregate<T, A, R> with(A seed, TryFunction2<? super A, ? super R, ? extends A> aggregate) {
    return new Aggregate<>(seed, it -> true, it -> (R) it, aggregate);
  }

  public static <T, A, R> Aggregate<T, A, R> with(A seed, TryPredicate1<? super T> where, TryFunction2<? super A, ? super R, ? extends A> aggregate) {
    return new Aggregate<>(seed, it -> true, it -> (R) it, aggregate);
  }
}
