package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.One;


public interface Averageable<T> extends Queryable<T> {
  default <N extends Number> One<Double> average(final TryFunction1<? super T, ? extends N> select) {
    record Average(Number folded, int index) {}

    return () -> cursor()
      .map(select)
      .reduce(new Average(0, -1), (index, left, next) -> new Average(left.folded.doubleValue() + next.doubleValue(), index))
      .map(it -> it.folded.doubleValue() / it.index);
  }

  default One<Double> average() {
    record Average(Number folded, int index) {}

    return () -> cursor()
      .map(it -> it instanceof Number number ? number : null)
      .reduce(new Average(0, -1), (index, left, next) -> new Average(left.folded.doubleValue() + next.doubleValue(), index))
      .map(it -> it.folded.doubleValue() / (it.index + 1));
  }
}



