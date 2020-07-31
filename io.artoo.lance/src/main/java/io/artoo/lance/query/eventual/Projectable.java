package io.artoo.lance.query.eventual;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.Eventual;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.query.operation.Select.as;

public interface Projectable<T> extends Queryable<T> {
  default <R> Eventual<R> select(final Func.Uni<? super T, ? extends R> select) {
    return () -> cursor().map(as(select));
  }

  default <R, E extends Eventual<R>> Eventual<R> selectEventual(final Func.Uni<? super T, ? extends E> select) {
    return () -> cursor().map(as(select)).flatMap(Queryable::cursor);
  }
}
