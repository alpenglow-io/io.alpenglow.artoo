package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.func.TryPredicate3;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.query.closure.Skip;
import io.alpenglow.artoo.lance.query.closure.Take;

public interface Partitionable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.OfTwo<A, B> skip(final int until) {
    return skipWhile((index, first, second) -> index < until);
  }

  default Many.OfTwo<A, B> skipWhile(final TryPredicate2<? super A, ? super B> where) {
    return skipWhile((index, first, second) -> where.test(first, second));
  }

  default Many.OfTwo<A, B> skipWhile(final TryPredicate3<? super Integer, ? super A, ? super B> where) {
    return () -> cursor().map(new Skip<>((index, pair) -> where.invoke(index, pair.first(), pair.second())));
  }

  default Many.OfTwo<A, B> take(final int until) {
    return takeWhile((index, first, second) -> index < until);
  }

  default Many.OfTwo<A, B> takeWhile(final TryPredicate2<? super A, ? super B> where) {
    return takeWhile((index, first, second) -> where.test(first, second));
  }

  default Many.OfTwo<A, B> takeWhile(final TryPredicate3<? super Integer, ? super A, ? super B> where) {
    return () -> cursor().map(new Take<>((index, pair) -> where.invoke(index, pair.first(), pair.second())));
  }
}
