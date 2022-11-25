package io.alpenglow.artoo.lance.query.one.oftwo;

import io.alpenglow.artoo.lance.func.TryBiPredicate;
import io.alpenglow.artoo.lance.func.TryTriPredicate;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.OfTwoTypes;
import io.alpenglow.artoo.lance.query.func.Where;

public interface Filterable<A, B> extends Queryable.OfTwo<A, B> {
  default One.OfTwo<A, B> where(final TryBiPredicate<? super A, ? super B> where) {
    return where((index, first, second) -> where.tryTest(first, second));
  }

  default One.OfTwo<A, B> where(final TryTriPredicate<? super Integer, ? super A, ? super B> where) {
    return () -> cursor().map(new Where<>((index, pair) -> where.tryTest(index, pair.first(), pair.second())));
  }

  default <X, Y> One.OfTwo<X, Y> ofTypes(final Class<? extends X> first, final Class<? extends Y> second) {
    return () -> cursor().map(it -> new OfTwoTypes<A, B, X, Y>(first, second).tryApply(it.first(), it.second()));
  }
}

