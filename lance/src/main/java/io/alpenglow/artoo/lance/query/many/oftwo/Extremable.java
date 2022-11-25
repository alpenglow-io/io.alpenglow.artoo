package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.func.TryBiFunction;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.Extreme;
import io.alpenglow.artoo.lance.tuple.Pair;

public interface Extremable<A, B> extends Queryable.OfTwo<A, B> {
  default <N extends Number> One<N> max(final TryBiFunction<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(rec(Extreme.<Pair<A, B>, N, N>max(pair -> select.tryApply(pair.first(), pair.second())))).keepNull();
  }

  default <N extends Number> One<N> min(final TryBiFunction<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(rec(Extreme.<Pair<A, B>, N, N>min(pair -> select.tryApply(pair.first(), pair.second())))).keepNull();
  }
}
