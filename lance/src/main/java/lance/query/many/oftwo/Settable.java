package lance.query.many.oftwo;

import lance.func.Pred;
import lance.query.Many;
import lance.Queryable;
import lance.query.func.Distinct;

public interface Settable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.OfTwo<A, B> distinct() {
    return distinct((first, second) -> true);
  }

  default Many.OfTwo<A, B> distinct(final Pred.Bi<? super A, ? super B> where) {
    return () -> cursor().map(new Distinct<>(pair -> where.tryTest(pair.first(), pair.second())));
  }
}
