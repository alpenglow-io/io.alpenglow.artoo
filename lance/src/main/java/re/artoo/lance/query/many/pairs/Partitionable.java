package re.artoo.lance.query.many.pairs;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.func.TryPredicate3;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.closure.Skip;
import re.artoo.lance.query.closure.Take;

public interface Partitionable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.Pairs<A, B> skip(final int until) {
    return skipWhile((index, first, second) -> index < until);
  }

  default Many.Pairs<A, B> skipWhile(final TryPredicate2<? super A, ? super B> where) {
    return skipWhile((index, first, second) -> where.test(first, second));
  }

  default Many.Pairs<A, B> skipWhile(final TryPredicate3<? super Integer, ? super A, ? super B> where) {
    return () -> cursor().map(new Skip<>((index, pair) -> where.invoke(index, pair.first(), pair.second())));
  }

  default Many.Pairs<A, B> take(final int until) {
    return takeWhile((index, first, second) -> index < until);
  }

  default Many.Pairs<A, B> takeWhile(final TryPredicate2<? super A, ? super B> where) {
    return takeWhile((index, first, second) -> where.test(first, second));
  }

  default Many.Pairs<A, B> takeWhile(final TryPredicate3<? super Integer, ? super A, ? super B> where) {
    return () -> cursor().map(new Take<>((index, pair) -> where.invoke(index, pair.first(), pair.second())));
  }
}
