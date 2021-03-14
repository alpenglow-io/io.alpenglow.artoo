package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.Select;
import io.artoo.lance.tuple.Pair;

public interface Projectable<A, B> extends Queryable.OfTwo<A, B> {
  default <R> Many<R> select(Func.Tri<? super Integer, ? super A, ? super B, ? extends R> select) {
    return () -> cursor().map(new Select<>((index, record) -> select.apply(index, record.first(), record.second())));
  }

  default <R> Many<R> select(Func.Bi<? super A, ? super B, ? extends R> select) {
    return select((index, first, second) -> select.tryApply(first, second));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(Func.Tri<? super Integer, ? super A, ? super B, ? extends Q> select) {
    return () -> cursor().map(new Select<Pair<A, B>, Q>((i, pair) -> select.tryApply(i, pair.first(), pair.second()))).flatMap(Queryable::cursor);
  }

  default <R, Q extends Queryable<R>> Many<R> selection(Func.Bi<? super A, ? super B, ? extends Q> select) {
    return selection((i, first, second) -> select.tryApply(first, second));
  }
}
