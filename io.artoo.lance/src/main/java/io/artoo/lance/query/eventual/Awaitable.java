package io.artoo.lance.query.eventual;

import io.artoo.lance.query.One;
import io.artoo.lance.query.Taskable;

public interface Awaitable<T> extends Taskable<T> {
  default One<T> await() {
    return () -> cursor().yield();
  }

  default One<T> await(int timeout) {
    return () -> cursor().asHand().yield(timeout);
  }
}
