package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.closure.Skip;
import re.artoo.lance.query.closure.Take;

public interface Partitionable<T> extends Queryable<T> {
  default Many<T> skip(final int until) {
    return skipWhile((index, it) -> index < until);
  }

  default Many<T> skipWhile(final TryPredicate1<? super T> where) {
    return skipWhile((index, it) -> where.test(it));
  }

  default Many<T> skipWhile(final TryPredicate2<? super Integer, ? super T> where) {
    return () -> cursor().map(new Skip<>(where));
  }

  default Many<T> take(final int until) {
    return takeWhile((index, it) -> index < until);
  }

  default Many<T> takeWhile(final TryPredicate1<? super T> where) {
    return takeWhile((index, param) -> where.test(param));
  }

  default Many<T> takeWhile(final TryPredicate2<? super Integer, ? super T> where) {
    return () -> cursor().map(new Take<>(where));
  }
}
