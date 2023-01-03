package re.artoo.lance.query.many.pairs;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.Count;

public interface Countable<A, B> extends Queryable.OfTwo<A, B> {
  default One<Integer> count() {
    return count((first, second) -> true);
  }

  default One<Integer> count(final TryPredicate2<? super A, ? super B> where) {
    return () -> cursor()
      .map(new Count<>(pair -> where.invoke(pair.first(), pair.second())))
      .or(() -> Cursor.open(0))
      .keepNull();
  }
}

