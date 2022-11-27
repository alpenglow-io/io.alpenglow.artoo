package io.alpenglow.artoo.lance.query.one;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.func.TrySupplier1;
import io.alpenglow.artoo.lance.func.tail.Select;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final TryFunction1<? super T, ? extends R> select) {
    return () -> cursor().map(rec(Select.with(select)));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(final TryFunction1<? super T, ? extends Q> selection) {
    return () -> cursor().map(rec(Select.with(selection))).flatMap(Queryable::cursor);
  }

  default <R> One<R> select(final TrySupplier1<? extends R> select) {
    return () -> cursor().map(rec(Select.with((i, it) -> select.tryGet())));
  }
}
