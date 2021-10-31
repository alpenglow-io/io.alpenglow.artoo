package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.Select;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(Func.MaybeBiFunction<? super Integer, ? super T, ? extends R> select) {
    return () -> cursor().map(as(Select.indexed(select)));
  }

  default <R> Many<R> select(Func.MaybeFunction<? super T, ? extends R> select) {
    return select((index, value) -> select.tryApply(value));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(Func.MaybeBiFunction<? super Integer, ? super T, ? extends Q> select) {
    return () -> cursor().map(as(Select.indexed(select))).flatMap(Queryable::cursor);
  }

  default <R, Q extends Queryable<R>> Many<R> selection(Func.MaybeFunction<? super T, ? extends Q> select) {
    return selection((i, it) -> select.tryApply(it));
  }
}
