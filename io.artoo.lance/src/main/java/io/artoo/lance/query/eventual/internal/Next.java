package io.artoo.lance.query.eventual.internal;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.eventual.Task;

public interface Next<T> {
  void await();
  <P> Next<P> select(final Func.Uni<? super T, ? extends P> select);
}
