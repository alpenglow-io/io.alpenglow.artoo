package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.func.tail.Aggregate;
import io.alpenglow.artoo.lance.query.One;

public interface Aggregatable<A, B> extends Countable<A, B>, Summable<A, B>, Averageable<A, B>, Extremable<A, B> {
  default <S, R> One<S> aggregate(final S seed, final TryPredicate2<? super A, ? super B> where, final TryFunction2<? super A, ? super B, ? extends R> select, final TryFunction2<? super S, ? super R, ? extends S> aggregate) {
    return cursor().map(rec(Aggregate.with(seed, it -> where.tryTest(it.first(), it.second()), it -> select.tryApply(it.first(), it.second()), aggregate)))::keepNull;
  }

  default <S, R> One<S> aggregate(final S seed, final TryFunction2<? super A, ? super B, ? extends R> select, final TryFunction2<? super S, ? super R, ? extends S> aggregate) {
    return aggregate(seed, (f, s) -> true, select, aggregate);
  }

  default <S, R> One<S> aggregate(final TryFunction2<? super A, ? super B, ? extends R> select, final TryFunction2<? super S, ? super R, ? extends S> aggregate) {
    return aggregate(null, (f, s) -> true, select, aggregate);
  }
}
