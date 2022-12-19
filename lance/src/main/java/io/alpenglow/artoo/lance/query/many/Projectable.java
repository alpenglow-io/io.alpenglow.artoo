package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.query.closure.Select;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(TryFunction2<? super Integer, ? super T, ? extends R> select) {
    return () -> cursor().map(Select.indexed(select));
  }

  default <R> Many<R> select(TryFunction1<? super T, ? extends R> select) {
    return () -> cursor().map(it -> select.invoke(it));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(TryFunction2<? super Integer, ? super T, ? extends Q> select) {
    return () -> cursor().map(Select.indexed(select)).flatMap(Queryable::cursor);
  }

  default <R, Q extends Queryable<R>> Many<R> selection(TryFunction1<? super T, ? extends Q> select) {
    return () -> cursor().map(select::invoke).flatMap(Queryable::cursor);
  }
}
