package re.artoo.lance.query.many.pairs;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Closure;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.At;
import re.artoo.lance.query.closure.First;
import re.artoo.lance.query.closure.Last;
import re.artoo.lance.query.closure.Single;

public interface Uniquable<A, B> extends Queryable.OfTwo<A, B> {
  default One.OfTwo<A, B> at(final int index) {
    return () -> cursor().map(Closure.at(index)).keepNull();
  }

  default One.OfTwo<A, B> first() {
    return first((first, second) -> true);
  }

  default One.OfTwo<A, B> first(final TryPredicate2<? super A, ? super B> where) {
    return () -> cursor().map(new First<>(pair -> where.invoke(pair.first(), pair.second()))).keepNull();
  }

  default One.OfTwo<A, B> last() {
    return last((first, second) -> true);
  }

  default One.OfTwo<A, B> last(final TryPredicate2<? super A, ? super B> where) {
    return () -> cursor().map(new Last<>(pair -> where.invoke(pair.first(), pair.second()))).keepNull();
  }

  default One.OfTwo<A, B> single() {
    return single((first, second) -> true);
  }

  default One.OfTwo<A, B> single(final TryPredicate2<? super A, ? super B> where) {
    return () -> cursor().map(new Single<>(pair -> where.invoke(pair.first(), pair.second()))).keepNull();
  }
}

