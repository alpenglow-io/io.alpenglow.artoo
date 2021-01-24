package io.artoo.lance.query.future.task;

import io.artoo.lance.func.Cons;
import io.artoo.lance.query.future.Futureable;
import io.artoo.lance.query.future.Task;
import io.artoo.lance.query.oper.Peek;

public interface Peekable<T> extends Futureable<T> {
  default Task<T> peek(final Cons.Uni<? super T> peek) {
    return () -> future().map(new Peek<>((i, it) -> peek.accept(it)));
  }

  default Task<T> exceptionally(Cons.Uni<? super Throwable> catch$) {
    return () -> future().onFailure(catch$::accept);
  }
}
