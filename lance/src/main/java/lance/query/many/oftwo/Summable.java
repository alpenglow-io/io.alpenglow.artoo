package lance.query.many.oftwo;

import lance.func.Func;
import lance.query.One;
import lance.Queryable;
import lance.query.func.Sum;
import lance.tuple.Pair;

public interface Summable<A, B> extends Queryable.OfTwo<A, B> {
  default <N extends Number> One<N> sum(final Func.MaybeBiFunction<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(new Sum<Pair<A, B>, N, N>(pair -> select.tryApply(pair.first(), pair.second()))).keepNull();
  }
}

