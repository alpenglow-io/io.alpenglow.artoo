package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.query.closure.Aggregate;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.tuple.Pair;

public interface Aggregatable<A, B> extends Countable<A, B>, Summable<A, B>, Averageable<A, B>, Extremable<A, B> {
  default <S, R> One<S> aggregate(final S seed, final TryPredicate2<? super A, ? super B> where, final TryFunction2<? super A, ? super B, ? extends R> select, final TryFunction2<? super S, ? super R, ? extends S> aggregate) {
    return () -> cursor().map(new Aggregate<Pair<A, B>, S, R>(seed, it -> where.invoke(it.first(), it.second()), it -> select.invoke(it.first(), it.second()), aggregate));
  }

  default <S, R> One<S> aggregate(final S seed, final TryFunction2<? super A, ? super B, ? extends R> select, final TryFunction2<? super S, ? super R, ? extends S> aggregate) {
    return aggregate(seed, (f, s) -> true, select, aggregate);
  }

  default <S, R> One<S> aggregate(final TryFunction2<? super A, ? super B, ? extends R> select, final TryFunction2<? super S, ? super R, ? extends S> aggregate) {
    return aggregate(null, (f, s) -> true, select, aggregate);
  }
}
