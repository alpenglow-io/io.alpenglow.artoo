package io.artoo.lance.query.eventual;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.Eventual;
import io.artoo.lance.query.Taskable;

import static io.artoo.lance.query.operation.Select.as;

public interface Projectable<T> extends Taskable<T> {
  default <R> Eventual<R> select(final Func.Uni<? super T, ? extends R> select) {
    return () -> cursor().map(as(select));
  }
}
