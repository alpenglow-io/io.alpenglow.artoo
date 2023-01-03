package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.closure.Average;
import re.artoo.lance.query.One;

public interface Averageable<T> extends Queryable<T> {
  default <N extends Number> One<Double> average(final TryFunction1<? super T, ? extends N> select) {
    return () -> cursor().map(new Average<>(select)).keepNull();
  }

  default One<Double> average() {
    return () -> cursor().map(new Average<>(it -> it)).keepNull();
  }
}



