package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.At;
import io.alpenglow.artoo.lance.query.func.First;
import io.alpenglow.artoo.lance.query.func.Last;
import io.alpenglow.artoo.lance.query.func.Single;

public interface Uniquable<A, B> extends Queryable.OfTwo<A, B> {
  default One.OfTwo<A, B> at(final int index) {
    return () -> cursor().map(new At<>(index)).keepNull();
  }

  default One.OfTwo<A, B> first() {
    return first((first, second) -> true);
  }

  default One.OfTwo<A, B> first(final TryPredicate2<? super A, ? super B> where) {
    return () -> cursor().map(new First<>(pair -> where.tryTest(pair.first(), pair.second()))).keepNull();
  }

  default One.OfTwo<A, B> last() {
    return last((first, second) -> true);
  }

  default One.OfTwo<A, B> last(final TryPredicate2<? super A, ? super B> where) {
    return () -> cursor().map(new Last<>(pair -> where.tryTest(pair.first(), pair.second()))).keepNull();
  }

  default One.OfTwo<A, B> single() {
    return single((first, second) -> true);
  }

  default One.OfTwo<A, B> single(final TryPredicate2<? super A, ? super B> where) {
    return () -> cursor().map(new Single<>(pair -> where.tryTest(pair.first(), pair.second()))).keepNull();
  }
}
