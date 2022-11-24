package lance.query.many.oftwo;

import lance.func.Pred;
import lance.literator.Cursor;
import lance.query.One;
import lance.Queryable;
import lance.query.func.Count;

public interface Countable<A, B> extends Queryable.OfTwo<A, B> {
  default One<Integer> count() {
    return count((first, second) -> true);
  }

  default One<Integer> count(final Pred.TryBiPredicate<? super A, ? super B> where) {
    return () -> cursor()
      .map(new Count<>(pair -> where.tryTest(pair.first(), pair.second())))
      .or(() -> Cursor.open(0))
      .keepNull();
  }
}

