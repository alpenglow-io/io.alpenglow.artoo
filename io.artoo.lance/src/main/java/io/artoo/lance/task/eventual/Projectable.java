package io.artoo.lance.task.eventual;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.task.Eventual;

import static io.artoo.lance.query.operation.Select.as;

public interface Projectable<T> extends Queryable<T> {
  default <R> Eventual<R> select(final Func.Uni<? super T, ? extends R> select) {
    return () -> cursor().map(as(select));
  }
}
