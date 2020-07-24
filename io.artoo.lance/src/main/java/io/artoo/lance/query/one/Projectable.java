package io.artoo.lance.query.one;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.query.operation.Select.as;
import static io.artoo.lance.type.Nullability.nonNullable;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final Func.Uni<? super T, ? extends R> select) {
    return () -> cursor().map(as(select));
  }

  default <R, O extends One<R>> One<R> selectOne(final Func.Uni<? super T, ? extends O> select) {
    return () -> cursor().map(as(select)).flatMap(Queryable::cursor);
  }
}
