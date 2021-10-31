package io.artoo.lance.query.eventual.task;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.eventual.Eventual;
import io.artoo.lance.query.eventual.Task;

public interface Projectable<T> extends Eventual<T> {
  default <R> Task<R> select(final Func.MaybeFunction<? super T, ? extends R> select) {
    return null;
  }

  default <R, O extends One<R>> Task<R> selection(final Func.MaybeFunction<? super T, ? extends O> selection) {
    return null;
  }
}
