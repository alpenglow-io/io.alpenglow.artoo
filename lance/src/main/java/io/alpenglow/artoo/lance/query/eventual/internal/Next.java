package io.alpenglow.artoo.lance.query.eventual.internal;

import io.alpenglow.artoo.lance.func.TryFunction;

public interface Next<T> {
  void await();
  <P> Next<P> select(final TryFunction<? super T, ? extends P> select);
}
