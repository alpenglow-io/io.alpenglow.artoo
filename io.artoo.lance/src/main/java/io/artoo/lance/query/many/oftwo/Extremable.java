package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.Extremum;
import io.artoo.lance.tuple.Pair;

public interface Extremable<A, B> extends Queryable.OfTwo<A, B> {
  private <N extends Number, V> One<V> extreme(int type, final Func.MaybeBiFunction<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(new Extremum<Pair<A, B>, N, V>(type, pair -> select.tryApply(pair.first(), pair.second()))).keepNull();
  }

  default <N extends Number> One<N> max(final Func.MaybeBiFunction<? super A, ? super B, ? extends N> select) {
    return extreme(1, select);
  }

  default <N extends Number> One<N> min(final Func.MaybeBiFunction<? super A, ? super B, ? extends N> select) {
    return extreme(-1, select);
  }
}
