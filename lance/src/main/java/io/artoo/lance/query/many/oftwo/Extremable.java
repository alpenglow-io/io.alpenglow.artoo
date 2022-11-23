package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.Extreme;
import io.artoo.lance.tuple.Pair;

public interface Extremable<A, B> extends Queryable.OfTwo<A, B> {
  default <N extends Number> One<N> max(final Func.MaybeBiFunction<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(rec(Extreme.<Pair<A, B>, N, N>max(pair -> select.tryApply(pair.first(), pair.second())))).keepNull();
  }

  default <N extends Number> One<N> min(final Func.MaybeBiFunction<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(rec(Extreme.<Pair<A, B>, N, N>min(pair -> select.tryApply(pair.first(), pair.second())))).keepNull();
  }
}
