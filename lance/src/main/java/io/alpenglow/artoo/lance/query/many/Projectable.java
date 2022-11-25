package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.func.TryBiFunction;
import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.func.tail.Select;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.Queryable;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(TryBiFunction<? super Integer, ? super T, ? extends R> select) {
    return () -> cursor().map(rec(Select.with(select)));
  }

  default <R> Many<R> select(TryFunction<? super T, ? extends R> select) {
    return select((index, value) -> select.tryApply(value));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(TryBiFunction<? super Integer, ? super T, ? extends Q> select) {
    return () -> cursor().map(rec(Select.with(select))).flatMap(Queryable::cursor);
  }

  default <R, Q extends Queryable<R>> Many<R> selection(TryFunction<? super T, ? extends Q> select) {
    return selection((i, it) -> select.tryApply(it));
  }
}
