package lance.query.many.oftwo;

import lance.func.Func;
import lance.query.One;
import lance.Queryable;
import lance.query.func.Extreme;
import lance.tuple.Pair;

public interface Extremable<A, B> extends Queryable.OfTwo<A, B> {
  default <N extends Number> One<N> max(final Func.TryBiFunction<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(rec(Extreme.<Pair<A, B>, N, N>max(pair -> select.tryApply(pair.first(), pair.second())))).keepNull();
  }

  default <N extends Number> One<N> min(final Func.TryBiFunction<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(rec(Extreme.<Pair<A, B>, N, N>min(pair -> select.tryApply(pair.first(), pair.second())))).keepNull();
  }
}
