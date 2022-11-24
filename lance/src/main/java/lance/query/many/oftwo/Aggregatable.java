package lance.query.many.oftwo;

import lance.func.Func;
import lance.func.Pred;
import lance.func.tail.Aggregate;
import lance.query.One;

public interface Aggregatable<A, B> extends Countable<A, B>, Summable<A, B>, Averageable<A, B>, Extremable<A, B> {
  default <S, R> One<S> aggregate(final S seed, final Pred.Bi<? super A, ? super B> where, final Func.MaybeBiFunction<? super A, ? super B, ? extends R> select, final Func.MaybeBiFunction<? super S, ? super R, ? extends S> aggregate) {
    return cursor().map(rec(Aggregate.with(seed, it -> where.tryTest(it.first(), it.second()), it -> select.tryApply(it.first(), it.second()), aggregate)))::keepNull;
  }

  default <S, R> One<S> aggregate(final S seed, final Func.MaybeBiFunction<? super A, ? super B, ? extends R> select, final Func.MaybeBiFunction<? super S, ? super R, ? extends S> aggregate) {
    return aggregate(seed, (f, s) -> true, select, aggregate);
  }

  default <S, R> One<S> aggregate(final Func.MaybeBiFunction<? super A, ? super B, ? extends R> select, final Func.MaybeBiFunction<? super S, ? super R, ? extends S> aggregate) {
    return aggregate(null, (f, s) -> true, select, aggregate);
  }
}
