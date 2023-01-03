package re.artoo.lance.query.many.pairs;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.query.closure.Average;
import re.artoo.lance.query.One;

public interface Averageable<A, B> extends Queryable.OfTwo<A, B> {
  default <N extends Number> One<Double> average(final TryFunction2<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(new Average<>(pair -> select.invoke(pair.first(), pair.second())));
  }
}
