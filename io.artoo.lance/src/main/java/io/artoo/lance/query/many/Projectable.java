package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(Func.Bi<? super Integer, ? super T, ? extends R> select) {
    return Many.wany(cursor().select(select));
  }

  default <R> Many<R> select(Func.Uni<? super T, ? extends R> select) {
    return Many.wany(cursor().select(select));
  }

  default <R, M extends Many<R>> Many<R> selectMany(Func.Bi<? super Integer, ? super T, ? extends M> select) {
    return Many.wany(cursor().selectNext((index, it) -> select.tryApply(index, it).cursor()));
  }

  default <R, M extends Many<R>> Many<R> selectMany(Func.Uni<? super T, ? extends M> select) {
    return Many.wany(cursor().selectNext(it -> select.tryApply(it).cursor()));
  }
}
