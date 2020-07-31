package io.artoo.lance.query.eventual;

import io.artoo.lance.query.One;

public interface Awaitable<T> extends io.artoo.lance.query.Queryable<T> {
  default One<T> await() {
    return () -> cursor().yield();
  }
}
