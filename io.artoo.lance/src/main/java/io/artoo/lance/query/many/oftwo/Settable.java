package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.Distinct;

public interface Settable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.OfTwo<A, B> distinct() {
    return distinct((first, second) -> true);
  }

  default Many.OfTwo<A, B> distinct(final Pred.Bi<? super A, ? super B> where) {
    return () -> cursor().map(new Distinct<>(pair -> where.tryTest(pair.first(), pair.second())));
  }
}
