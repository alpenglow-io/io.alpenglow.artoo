package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.closure.Average;
import io.alpenglow.artoo.lance.query.One;

public interface Averageable<T> extends Queryable<T> {
  default <N extends Number> One<Double> average(final TryFunction1<? super T, ? extends N> select) {
    return () -> cursor().map(new Average<>(select));
  }

  default One<Double> average() {
    return average(it -> it instanceof Number n ? n.doubleValue() : null);
  }
}



