package io.artoo.lance.query.one;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.Select;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final Func.Uni<? super T, ? extends R> select) {
    return () -> cursor().map(as(Select.indexed(select)));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(final Func.Uni<? super T, ? extends Q> selection) {
    return () -> cursor().map(as(Select.indexed((i, it) -> selection.tryApply(it)))).flatMap(Queryable::cursor);
  }

  default <R> One<R> select(final Suppl.Uni<? extends R> select) {
    return () -> cursor().map(as(Select.indexed((i, it) -> select.tryGet())));
  }
}
