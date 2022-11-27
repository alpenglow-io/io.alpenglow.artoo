package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.Distinct;

public interface Settable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.OfTwo<A, B> distinct() {
    return distinct((first, second) -> true);
  }

  default Many.OfTwo<A, B> distinct(final TryPredicate2<? super A, ? super B> where) {
    return () -> cursor().map(new Distinct<>(pair -> where.tryTest(pair.first(), pair.second())));
  }
}
