package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.internal.Aggregate;
import io.artoo.lance.tuple.Pair;

public interface Aggregatable<A, B> extends Countable<A, B>, Summable<A, B>, Averageable<A, B>, Extremable<A, B> {
  default <S, R> One<S> aggregate(final S seed, final Pred.Bi<? super A, ? super B> where, final Func.Bi<? super A, ? super B, ? extends R> select, final Func.Bi<? super S, ? super R, ? extends S> aggregate) {
    return () -> cursor().map(new Aggregate<Pair<A, B>, S, R>(seed, it -> where.tryTest(it.first(), it.second()), it -> select.tryApply(it.first(), it.second()), aggregate)).keepNull();
  }

  default <S, R> One<S> aggregate(final S seed, final Func.Bi<? super A, ? super B, ? extends R> select, final Func.Bi<? super S, ? super R, ? extends S> aggregate) {
    return aggregate(seed, (f, s) -> true, select, aggregate);
  }

  default <S, R> One<S> aggregate(final Func.Bi<? super A, ? super B, ? extends R> select, final Func.Bi<? super S, ? super R, ? extends S> aggregate) {
    return aggregate(null, (f, s) -> true, select, aggregate);
  }
}
