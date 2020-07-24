package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.query.operation.Index.indexed;
import static io.artoo.lance.query.operation.Select.as;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(Func.Bi<? super Integer, ? super T, ? extends R> select) {
    return () -> cursor().map(as(indexed(), select).butNulls());
  }

  default <R> Many<R> select(Func.Uni<? super T, ? extends R> select) {
    return () -> cursor().map(as(select).butNulls());
  }

  default <R, M extends Many<R>> Many<R> selectMany(Func.Bi<? super Integer, ? super T, ? extends M> select) {
    return () -> cursor().map(as(indexed(), select).butNulls()).flatMap(Queryable::cursor);
  }

  default <R, M extends Many<R>> Many<R> selectMany(Func.Uni<? super T, ? extends M> select) {
    return () -> cursor().map(as(select)).flatMap(Queryable::cursor);
  }
}
