package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.oper.Aggregate;

public interface Aggregatable<T> extends Countable<T>, Summable<T>, Averageable<T>, Extremable<T> {
  default <A, R> One<A> aggregate(final A seed, final Pred.Uni<? super T> where, final Func.Uni<? super T, ? extends R> select, final Func.Bi<? super A, ? super R, ? extends A> aggregate) {
    return () -> cursor().map(new Aggregate<>(seed, where, select, aggregate)).scroll();
  }

  default <A, R> One<A> aggregate(final A seed, final Func.Uni<? super T, ? extends R> select, final Func.Bi<? super A, ? super R, ? extends A> aggregate) {
    return aggregate(seed, it -> true, select, aggregate);
  }

  default <A, R> One<A> aggregate(final Func.Uni<? super T, ? extends R> select, final Func.Bi<? super A, ? super R, ? extends A> aggregate) {
    return aggregate(null, it -> true, select, aggregate);
  }

  default <A> One<A> aggregate(final A seed, final Func.Bi<? super A, ? super T, ? extends A> aggregate) {
    return aggregate(seed, it -> true, it -> it, aggregate);
  }

  default One<T> aggregate(final Func.Bi<? super T, ? super T, ? extends T> aggregate) {
    return aggregate(null, it -> true, it -> it, aggregate);
  }
}

