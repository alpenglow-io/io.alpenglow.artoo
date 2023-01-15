package re.artoo.lance.query.many.pairs;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.closure.Distinct;

public interface Settable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.Pairs<A, B> distinct() {
    return distinct((first, second) -> true);
  }

  default Many.Pairs<A, B> distinct(final TryPredicate2<? super A, ? super B> where) {
    return () -> cursor().map(new Distinct<>(pair -> where.invoke(pair.first(), pair.second())));
  }
}
