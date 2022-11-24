package lance.query.many.oftwo;

import lance.func.Pred;
import lance.literator.Cursor;
import lance.query.One;
import lance.Queryable;
import lance.query.func.Every;
import lance.query.func.Some;

public interface Quantifiable<A, B> extends Queryable.OfTwo<A, B> {
  default <X, Y> One<Boolean> all(final Class<X> type1, final Class<Y> type2) {
    return all((first, second) -> type1.isInstance(first) && type2.isInstance(type2));
  }

  default One<Boolean> all(final Pred.TryBiPredicate<? super A, ? super B> where) {
    return () -> cursor().map(new Every<>(pair -> where.tryTest(pair.first(), pair.second()))).keepNull();
  }

  default One<Boolean> any() { return this.any((first, second) -> true); }

  default One<Boolean> any(final Pred.TryBiPredicate<? super A, ? super B> where) {
    return () -> cursor().map(new Some<>(pair -> where.tryTest(pair.first(), pair.second()))).keepNull().or(() -> Cursor.open(false));
  }
}


