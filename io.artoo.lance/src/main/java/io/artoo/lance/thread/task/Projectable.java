package io.artoo.lance.thread.task;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.thread.Task;

import static io.artoo.lance.query.operation.Select.as;

public interface Projectable<T> extends Queryable<T> {
  default <R> Task<R> select(final Func.Uni<? super T, ? extends R> select) {
    return () -> cursor().map(as(select));
  }
}
