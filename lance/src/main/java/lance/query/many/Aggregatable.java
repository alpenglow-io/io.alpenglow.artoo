package lance.query.many;

import lance.func.Func.MaybeBiFunction;
import lance.func.Func.MaybeFunction;
import lance.func.Pred;
import lance.func.tail.Aggregate;
import lance.query.One;

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

