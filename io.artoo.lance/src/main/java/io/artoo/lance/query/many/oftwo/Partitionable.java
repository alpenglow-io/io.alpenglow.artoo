package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.Skip;
import io.artoo.lance.query.internal.Take;
import io.artoo.lance.tuple.Pair;

public interface Partitionable<A, B> extends Queryable.OfTwo<A, B> {
  default Many.OfTwo<A, B> skip(final int until) {
    return skipWhile((index, first, second) -> index < until);
  }

  default Many.OfTwo<A, B> skipWhile(final Pred.Bi<? super A, ? super B> where) {
    return skipWhile((index, first, second) -> where.test(first, second));
  }

  default Many.OfTwo<A, B> skipWhile(final Pred.Tri<? super Integer, ? super A, ? super B> where) {
    return () -> cursor().map(new Skip<Pair<A, B>, Pair<A, B>>((index, pair) -> where.tryTest(index, pair.first(), pair.second())));
  }

  default Many.OfTwo<A, B> take(final int until) {
    return takeWhile((index, first, second) -> index < until);
  }

  default Many.OfTwo<A, B> takeWhile(final Pred.Bi<? super A, ? super B> where) {
    return takeWhile((index, first, second) -> where.test(first, second));
  }

  default Many.OfTwo<A, B> takeWhile(final Pred.Tri<? super Integer, ? super A, ? super B> where) {
    return () -> cursor().map(new Take<Pair<A, B>, Pair<A, B>>((index, pair) -> where.tryTest(index, pair.first(), pair.second())));
  }
}
