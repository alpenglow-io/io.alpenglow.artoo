package re.artoo.lance.query.many.pairs;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.Every;
import re.artoo.lance.query.closure.Some;

public interface Quantifiable<A, B> extends Queryable.OfTwo<A, B> {
  default <X, Y> One<Boolean> all(final Class<X> type1, final Class<Y> type2) {
    return all((first, second) -> type1.isInstance(first) && type2.isInstance(type2));
  }

  default One<Boolean> all(final TryPredicate2<? super A, ? super B> where) {
    return () -> cursor().map(new Every<>(pair -> where.invoke(pair.first(), pair.second()))).keepNull();
  }

  default One<Boolean> any() { return this.any((first, second) -> true); }

  default One<Boolean> any(final TryPredicate2<? super A, ? super B> where) {
    return () -> cursor().map(new Some<>(pair -> where.invoke(pair.first(), pair.second()))).keepNull().or(() -> Cursor.open(false));
  }
}


