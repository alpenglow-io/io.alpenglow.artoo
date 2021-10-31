package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.Every;
import io.artoo.lance.query.func.Some;

public interface Quantifiable<A, B> extends Queryable.OfTwo<A, B> {
  default <X, Y> One<Boolean> all(final Class<X> type1, final Class<Y> type2) {
    return all((first, second) -> type1.isInstance(first) && type2.isInstance(type2));
  }

  default One<Boolean> all(final Pred.Bi<? super A, ? super B> where) {
    return () -> cursor().map(new Every<>(pair -> where.tryTest(pair.first(), pair.second()))).keepNull();
  }

  default One<Boolean> any() { return this.any((first, second) -> true); }

  default One<Boolean> any(final Pred.Bi<? super A, ? super B> where) {
    return () -> cursor().map(new Some<>(pair -> where.tryTest(pair.first(), pair.second()))).keepNull().or(() -> Cursor.open(false));
  }
}


