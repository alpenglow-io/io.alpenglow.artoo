package io.artoo.lance.query.future;

import io.artoo.lance.func.Cons;
import io.vertx.core.Future;

@FunctionalInterface
public interface Futureable<T> {
  default void eventually(Cons.Uni<? super T> cons) {
    future().onComplete(it -> cons.accept(it.result()));
  }

  Future<T> future();
}
