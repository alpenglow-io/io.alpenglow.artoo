package re.artoo.lance.query.one.oftwo;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.func.TryPredicate3;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.OfTwoTypes;
import re.artoo.lance.query.closure.Where;

public interface Filterable<A, B> extends Queryable.OfTwo<A, B> {
  default One.OfTwo<A, B> where(final TryPredicate2<? super A, ? super B> where) {
    return where((index, first, second) -> where.invoke(first, second));
  }

  default One.OfTwo<A, B> where(final TryPredicate3<? super Integer, ? super A, ? super B> where) {
    return () -> cursor().map(new Where<>((index, pair) -> where.invoke(index, pair.first(), pair.second())));
  }

  default <X, Y> One.OfTwo<X, Y> ofTypes(final Class<? extends X> first, final Class<? extends Y> second) {
    return () -> cursor().map(it -> new OfTwoTypes<A, B, X, Y>(first, second).invoke(it.first(), it.second()));
  }
}

