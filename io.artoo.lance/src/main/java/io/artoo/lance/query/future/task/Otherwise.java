package io.artoo.lance.query.future.task;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.future.Futureable;
import io.artoo.lance.query.future.Task;

import static io.vertx.core.Future.failedFuture;

public interface Otherwise<T> extends Futureable<T> {
  default Task<T> or(final T element) {
    return () -> future().otherwise(element);
  }

  default <O extends Task<T>> Task<T> or(final O otherwise) {
    return () -> future().recover(it -> otherwise.future());
  }

  default <E extends RuntimeException> Task<T> or(final String message, final Func.Bi<? super String, ? super Throwable, ? extends E> exception) {
    return () -> future().recover(it -> failedFuture(exception.apply(message, it)));
  }

  default <E extends RuntimeException> Task<T> or(final Suppl.Uni<? extends E> exception) {
    return () -> future().recover(it -> failedFuture(exception.get()));
  }
}
