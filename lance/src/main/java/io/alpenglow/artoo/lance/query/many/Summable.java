package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.query.closure.Sum;

public interface Summable<T> extends Queryable<T> {
  default <N extends Number> One<N> sum(final TryFunction1<? super T, ? extends N> select) {
    return () -> cursor().map(new Sum<T, N, N>(select)).keepNull();
  }

  default One<T> sum() {
    return () -> cursor().map(new Sum<T, Object, T>(it -> it)).keepNull();
  }
}

