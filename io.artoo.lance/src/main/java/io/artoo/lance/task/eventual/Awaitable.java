package io.artoo.lance.task.eventual;

import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;

public interface Awaitable<T> extends Queryable<T> {
  default One<T> await() {
    return () -> cursor().yield();
  }
}
