package re.artoo.lance.query.many.pairs;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.Sum;
import re.artoo.lance.tuple.Pair;

public interface Summable<A, B> extends Queryable.OfTwo<A, B> {
  default <N extends Number> One<N> sum(final TryFunction2<? super A, ? super B, ? extends N> select) {
    return () -> cursor().map(new Sum<Pair<A, B>, N, N>(pair -> select.invoke(pair.first(), pair.second()))).keepNull();
  }
}

