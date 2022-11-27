package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.Sum;
import io.alpenglow.artoo.lance.tuple.Pair;

public interface Summable<A, B> extends Queryable.OfTwo<A, B> {
  default <N extends Number> One<N> sum(final TryFunction2<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(new Sum<Pair<A, B>, N, N>(pair -> select.tryApply(pair.first(), pair.second()))).keepNull();
  }
}

