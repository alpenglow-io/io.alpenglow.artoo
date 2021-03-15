package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.All;
import io.artoo.lance.query.internal.Any;

public interface Quantifiable<A, B> extends Queryable.OfTwo<A, B> {
  default <X, Y> One<Boolean> all(final Class<X> type1, final Class<Y> type2) {
    return all((first, second) -> type1.isInstance(first) && type2.isInstance(type2));
  }

  default One<Boolean> all(final Pred.Bi<? super A, ? super B> where) {
    return () -> cursor().map(new All<>(pair -> where.tryTest(pair.first(), pair.second()))).walkDown();
  }

  default One<Boolean> any() { return this.any((first, second) -> true); }

  default One<Boolean> any(final Pred.Bi<? super A, ? super B> where) {
    return () -> cursor().map(new Any<>(pair -> where.tryTest(pair.first(), pair.second()))).walkDown().or(() -> Cursor.open(false));
  }
}


