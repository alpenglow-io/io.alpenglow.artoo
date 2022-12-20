package io.alpenglow.artoo.lance.query.many.pairs;

import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.query.closure.Extreme;
import io.alpenglow.artoo.lance.tuple.Pair;

public interface Extremable<A, B> extends Queryable.OfTwo<A, B> {
  default <N extends Number> One<N> max(final TryFunction2<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(Extreme.<Pair<A, B>, N, N>max(pair -> select.invoke(pair.first(), pair.second())));
  }

  default <N extends Number> One<N> min(final TryFunction2<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(Extreme.<Pair<A, B>, N, N>min(pair -> select.invoke(pair.first(), pair.second())));
  }
}
