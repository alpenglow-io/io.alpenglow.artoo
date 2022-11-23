package io.artoo.lance.query.many;

import io.artoo.lance.func.Func.MaybeBiFunction;
import io.artoo.lance.func.Func.MaybeFunction;
import io.artoo.lance.func.Pred;
import io.artoo.lance.func.tail.Aggregate;
import io.artoo.lance.query.One;

public interface Aggregatable<T> extends Countable<T>, Summable<T>, Averageable<T>, Extremable<T> {
  default <A, R> One<A> aggregate(final A seed, final Pred.MaybePredicate<? super T> where, final MaybeFunction<? super T, ? extends R> select, final MaybeBiFunction<? super A, ? super R, ? extends A> aggregate) {
    return () -> cursor().map(rec(Aggregate.with(seed, where, select, aggregate))).keepNull();
  }

  default <A, R> One<A> aggregate(final A seed, final MaybeFunction<? super T, ? extends R> select, final MaybeBiFunction<? super A, ? super R, ? extends A> aggregate) {
    return aggregate(seed, it -> true, select, aggregate);
  }

  default <A, R> One<A> aggregate(final MaybeFunction<? super T, ? extends R> select, final MaybeBiFunction<? super A, ? super R, ? extends A> aggregate) {
    return aggregate(null, it -> true, select, aggregate);
  }

  default <A> One<A> aggregate(final A seed, final MaybeBiFunction<? super A, ? super T, ? extends A> aggregate) {
    return aggregate(seed, it -> true, it -> it, aggregate);
  }

  default One<T> aggregate(final MaybeBiFunction<? super T, ? super T, ? extends T> aggregate) {
    return aggregate(null, it -> true, it -> it, aggregate);
  }

}

