package io.artoo.lance.query.future.task;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.future.Futureable;
import io.artoo.lance.query.future.Task;
import io.artoo.lance.query.oper.NotOfType;
import io.artoo.lance.query.oper.OfType;
import io.artoo.lance.query.oper.Where;

@FunctionalInterface
public interface Filterable<T> extends Futureable<T> {
  default Task<T> where(final Pred.Uni<? super T> where) {
    return () -> future().map(new Where<>((i, it) -> where.tryTest(it)));
  }

  default <R> Task<R> ofType(final Class<? extends R> type) {
    return () -> future().map(new OfType<>(type));
  }

  default <R> Task<T> notOfType(final Class<? extends R> type) {
    return () -> future().map(new NotOfType<>(type));
  }
}
