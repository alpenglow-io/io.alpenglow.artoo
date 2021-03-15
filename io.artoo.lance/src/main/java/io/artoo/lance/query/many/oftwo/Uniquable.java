package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.At;
import io.artoo.lance.query.internal.First;
import io.artoo.lance.query.internal.Last;
import io.artoo.lance.query.internal.Single;

public interface Uniquable<A, B> extends Queryable.OfTwo<A, B> {
  default One.OfTwo<A, B> at(final int index) {
    return () -> cursor().map(new At<>(index)).walkDown();
  }

  default One.OfTwo<A, B> first() {
    return first((first, second) -> true);
  }

  default One.OfTwo<A, B> first(final Pred.Bi<? super A, ? super B> where) {
    return () -> cursor().map(new First<>(pair -> where.tryTest(pair.first(), pair.second()))).walkDown();
  }

  default One.OfTwo<A, B> last() {
    return last((first, second) -> true);
  }

  default One.OfTwo<A, B> last(final Pred.Bi<? super A, ? super B> where) {
    return () -> cursor().map(new Last<>(pair -> where.tryTest(pair.first(), pair.second()))).walkDown();
  }

  default One.OfTwo<A, B> single() {
    return single((first, second) -> true);
  }

  default One.OfTwo<A, B> single(final Pred.Bi<? super A, ? super B> where) {
    return () -> cursor().map(new Single<>(pair -> where.tryTest(pair.first(), pair.second()))).walkDown();
  }
}

