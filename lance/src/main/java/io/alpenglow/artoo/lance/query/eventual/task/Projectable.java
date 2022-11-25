package io.alpenglow.artoo.lance.query.eventual.task;

import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.query.eventual.Eventual;
import io.alpenglow.artoo.lance.query.eventual.Task;

public interface Projectable<T> extends Eventual<T> {
  default <R> Task<R> select(final TryFunction<? super T, ? extends R> select) {
    return null;
  }

  default <R, O extends One<R>> Task<R> selection(final TryFunction<? super T, ? extends O> selection) {
    return null;
  }
}
