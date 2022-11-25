package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.func.TryBiFunction;
import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.func.TryPredicate;
import io.alpenglow.artoo.lance.func.tail.Aggregate;
import io.alpenglow.artoo.lance.query.One;

public interface Aggregatable<T> extends Countable<T>, Summable<T>, Averageable<T>, Extremable<T> {
  default <A, R> One<A> aggregate(final A seed, final TryPredicate<? super T> where, final TryFunction<? super T, ? extends R> select, final TryBiFunction<? super A, ? super R, ? extends A> aggregate) {
    return () -> cursor().map(rec(Aggregate.with(seed, where, select, aggregate))).keepNull();
  }

  default <A, R> One<A> aggregate(final A seed, final TryFunction<? super T, ? extends R> select, final TryBiFunction<? super A, ? super R, ? extends A> aggregate) {
    return aggregate(seed, it -> true, select, aggregate);
  }

  default <A, R> One<A> aggregate(final TryFunction<? super T, ? extends R> select, final TryBiFunction<? super A, ? super R, ? extends A> aggregate) {
    return aggregate(null, it -> true, select, aggregate);
  }

  default <A> One<A> aggregate(final A seed, final TryBiFunction<? super A, ? super T, ? extends A> aggregate) {
    return aggregate(seed, it -> true, it -> it, aggregate);
  }

  default One<T> aggregate(final TryBiFunction<? super T, ? super T, ? extends T> aggregate) {
    return aggregate(null, it -> true, it -> it, aggregate);
  }

}

