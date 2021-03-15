package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.Extremum;
import io.artoo.lance.tuple.Pair;

public interface Extremable<A, B> extends Queryable.OfTwo<A, B> {
  private <N extends Number, V> One<V> extreme(int type, final Func.Bi<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(new Extremum<Pair<A, B>, N, V>(type, pair -> select.tryApply(pair.first(), pair.second()))).walkDown();
  }

  default <N extends Number> One<N> max(final Func.Bi<? super A, ? super B, ? extends N> select) {
    return extreme(1, select);
  }

  default <N extends Number> One<N> min(final Func.Bi<? super A, ? super B, ? extends N> select) {
    return extreme(-1, select);
  }
}
