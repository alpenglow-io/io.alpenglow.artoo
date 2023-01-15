package re.artoo.lance.query.many.pairs;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.Extreme;
import re.artoo.lance.tuple.Pair;

public interface Extremable<A, B> extends Queryable.OfTwo<A, B> {
  default <N extends Number> One<N> max(final TryFunction2<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(Extreme.<Pair<A, B>, N>max(pair -> select.invoke(pair.first(), pair.second())));
  }

  default <N extends Number> One<N> min(final TryFunction2<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(Extreme.<Pair<A, B>, N>min(pair -> select.invoke(pair.first(), pair.second())));
  }
}
