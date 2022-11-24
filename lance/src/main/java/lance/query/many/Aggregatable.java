package lance.query.many;

import lance.func.Func.TryBiFunction;
import lance.func.Func.TryFunction;
import lance.func.Pred;
import lance.func.tail.Aggregate;
import lance.query.One;

public interface Aggregatable<T> extends Countable<T>, Summable<T>, Averageable<T>, Extremable<T> {
  default <A, R> One<A> aggregate(final A seed, final Pred.TryPredicate<? super T> where, final TryFunction<? super T, ? extends R> select, final TryBiFunction<? super A, ? super R, ? extends A> aggregate) {
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

