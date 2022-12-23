package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.query.closure.Extreme;

public interface Extremable<T> extends Queryable<T> {
  default <N extends Number> One<N> max(final TryFunction1<? super T, ? extends N> select) {
    return () -> cursor().map(Extreme.<T, N>max(select)).keepNull();
  }

  default One<T> max() {
    return () -> cursor().map(Extreme.max()).keepNull();
  }

  default <N extends Number> One<N> min(final TryFunction1<? super T, ? extends N> select) {
    return () -> cursor().map(Extreme.<T, N>min(select)).keepNull();
  }

  default One<T> min() {
    return () -> cursor().map(Extreme.min()).keepNull();
  }
}

