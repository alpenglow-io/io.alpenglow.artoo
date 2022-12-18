package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.closure.Extreme;

public interface Extremable<T> extends Queryable<T> {
  private One<T> extreme() {
    return this.extreme(it -> it instanceof Number n ? n : null);
  }

  private <N extends Number, V> One<V> extreme(final TryFunction1<? super T, ? extends N> select) {
    return () -> cursor().map(Extreme.<T, N, V>max(select));
  }

  default <N extends Number> One<N> max(final TryFunction1<? super T, ? extends N> select) {
    return () -> cursor().map(Extreme.<T, N, N>max(select));
  }

  default One<T> max() {
    return () -> cursor().map(Extreme.<T, Number, T>max());
  }

  default <N extends Number> One<N> min(final TryFunction1<? super T, ? extends N> select) {
    return () -> cursor().map(Extreme.<T, N, N>min(select));
  }

  default One<T> min() {
    return () -> cursor().map(Extreme.<T, Number, T>min());
  }
}

