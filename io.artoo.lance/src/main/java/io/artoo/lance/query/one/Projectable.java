package io.artoo.lance.query.one;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.operation.Select;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final Func.Uni<? super T, ? extends R> select) {
    final var s = nonNullable(select, "select");
    return () -> cursor().map(new Select<>((i, it) -> s.tryApply(it)));
  }

  default <R, O extends One<R>> One<R> selectOne(final Func.Uni<? super T, ? extends O> selectOne) {
    final var s = nonNullable(selectOne, "selectOne");
    return () -> cursor().map(new Select<>((i, it) -> s.tryApply(it))).flatMap(Queryable::cursor);
  }
}
