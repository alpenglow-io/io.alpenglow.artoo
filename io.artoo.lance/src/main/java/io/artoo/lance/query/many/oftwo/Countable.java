package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.Count;

public interface Countable<A, B> extends Queryable.OfTwo<A, B> {
  default One<Integer> count() {
    return count((first, second) -> true);
  }

  default One<Integer> count(final Pred.Bi<? super A, ? super B> where) {
    return () -> cursor()
      .map(new Count<>(pair -> where.tryTest(pair.first(), pair.second())))
      .or(() -> Cursor.open(0))
      .keepNull();
  }
}

