package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.func.tail.Average;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;

public interface Averageable<A, B> extends Queryable.OfTwo<A, B> {
  default <N extends Number> One<Double> average(final TryFunction2<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(rec(new Average<>(pair -> select.tryApply(pair.first(), pair.second())))).keepNull();
  }
}
