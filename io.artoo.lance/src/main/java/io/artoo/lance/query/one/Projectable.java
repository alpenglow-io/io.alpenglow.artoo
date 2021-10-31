package io.artoo.lance.query.one;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.Select;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final Func.MaybeFunction<? super T, ? extends R> select) {
    return () -> cursor().map(as(Select.indexed(select)));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(final Func.MaybeFunction<? super T, ? extends Q> selection) {
    return () -> cursor().map(as(Select.indexed((i, it) -> selection.tryApply(it)))).flatMap(Queryable::cursor);
  }

  default <R> One<R> select(final Suppl.MaybeSupplier<? extends R> select) {
    return () -> cursor().map(as(Select.indexed((i, it) -> select.tryGet())));
  }
}
