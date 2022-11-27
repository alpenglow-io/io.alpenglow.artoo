package io.alpenglow.artoo.lance.query.eventual.internal;

import io.alpenglow.artoo.lance.func.TryFunction1;

public interface Next<T> {
  void await();
  <P> Next<P> select(final TryFunction1<? super T, ? extends P> select);
}
