package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.One;

public interface Countable<T> extends Queryable<T> {
  default One<Integer> count() {
    return () -> cursor().fold(0, (counted, element) -> counted + 1);
  }

  default One<Integer> count(TryIntPredicate1<? super T> where) {
    return () -> cursor().filter(where).fold(0, (counted, element) -> counted + 1);
  }
  default One<Integer> count(final TryPredicate1<? super T> where) {
    return count((index, it) -> where.invoke(it));
  }
}

