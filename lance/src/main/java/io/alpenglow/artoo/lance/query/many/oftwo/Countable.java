package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.Count;

public interface Countable<A, B> extends Queryable.OfTwo<A, B> {
  default One<Integer> count() {
    return count((first, second) -> true);
  }

  default One<Integer> count(final TryPredicate2<? super A, ? super B> where) {
    return () -> cursor()
      .map(new Count<>(pair -> where.tryTest(pair.first(), pair.second())))
      .or(() -> Cursor.open(0))
      .keepNull();
  }
}

