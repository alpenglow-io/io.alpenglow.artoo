package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.OfTwoTypes;
import io.artoo.lance.query.func.Where;

public interface Filterable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.OfTwo<A, B> where(final Pred.Bi<? super A, ? super B> where) {
    return where((index, first, second) -> where.tryTest(first, second));
  }

  default Many.OfTwo<A, B> where(final Pred.Tri<? super Integer, ? super A, ? super B> where) {
    return () -> cursor().map(new Where<>((index, pair) -> where.tryTest(index, pair.first(), pair.second())));
  }

  default <X, Y> Many.OfTwo<X, Y> ofTypes(final Class<? extends X> first, final Class<? extends Y> second) {
    return () -> cursor().map(it -> new OfTwoTypes<A, B, X, Y>(first, second).tryApply(it.first(), it.second()));
  }
}

