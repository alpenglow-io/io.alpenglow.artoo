package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.operation.Select;

import java.util.function.Function;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(Func.Bi<? super Integer, ? super T, ? extends R> select) {
    final var s = nonNullable(select, "select");
    return () -> cursor().map(new Select<>(s));
  }

  default <R> Many<R> select(Func.Uni<? super T, ? extends R> select) {
    final var s = nonNullable(select, "select");
    return select((index, value) -> s.tryApply(value));
  }

  default <R, M extends Many<R>> Many<R> selectMany(Func.Bi<? super Integer, ? super T, ? extends M> select) {
    final var s = nonNullable(select, "select");
    return () -> cursor().map(new Select<T, M>(s)).flatMap(Queryable::cursor);
  }

  default <R, M extends Many<R>> Many<R> selectMany(Func.Uni<? super T, ? extends M> select) {
    final var s = nonNullable(select, "select");
    return selectMany((i, it) -> s.tryApply(it));
  }
}

