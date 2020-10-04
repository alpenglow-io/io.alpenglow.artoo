package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.oper.Select;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(Func.Bi<? super Integer, ? super T, ? extends R> select) {
    return () -> cursor().map(new Select<>(select));
  }

  default <R> Many<R> select(Func.Uni<? super T, ? extends R> select) {
    return select((index, value) -> select.tryApply(value));
  }

  default <R, M extends Many<R>> Many<R> selectMany(Func.Bi<? super Integer, ? super T, ? extends M> select) {
    return () -> cursor().map(new Select<T, M>(select)).flatMap(Queryable::cursor);
  }

  default <R, M extends Many<R>> Many<R> selectMany(Func.Uni<? super T, ? extends M> select) {
    return selectMany((i, it) -> select.tryApply(it));
  }
}
