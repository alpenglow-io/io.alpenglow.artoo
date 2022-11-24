package lance.query.many.oftwo;

import lance.func.Pred;
import lance.query.One;
import lance.Queryable;
import lance.query.func.At;
import lance.query.func.First;
import lance.query.func.Last;
import lance.query.func.Single;

public interface Uniquable<A, B> extends Queryable.OfTwo<A, B> {
  default One.OfTwo<A, B> at(final int index) {
    return () -> cursor().map(new At<>(index)).keepNull();
  }

  default One.OfTwo<A, B> first() {
    return first((first, second) -> true);
  }

  default One.OfTwo<A, B> first(final Pred.Bi<? super A, ? super B> where) {
    return () -> cursor().map(new First<>(pair -> where.tryTest(pair.first(), pair.second()))).keepNull();
  }

  default One.OfTwo<A, B> last() {
    return last((first, second) -> true);
  }

  default One.OfTwo<A, B> last(final Pred.Bi<? super A, ? super B> where) {
    return () -> cursor().map(new Last<>(pair -> where.tryTest(pair.first(), pair.second()))).keepNull();
  }

  default One.OfTwo<A, B> single() {
    return single((first, second) -> true);
  }

  default One.OfTwo<A, B> single(final Pred.Bi<? super A, ? super B> where) {
    return () -> cursor().map(new Single<>(pair -> where.tryTest(pair.first(), pair.second()))).keepNull();
  }
}

