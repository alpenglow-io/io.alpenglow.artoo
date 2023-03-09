package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.One;
import re.artoo.lance.tuple.Tuple;


public interface Averageable<T> extends Queryable<T> {
  default <N extends Number> One<Double> average(final TryFunction1<? super T, ? extends N> select) {
    return () -> cursor()
      .map(select)
      .nonNull()
      .fold(
        Tuple.of(0.0, 0),
        (acc, element) -> acc.let((folded, count) -> Tuple.of(folded + element.doubleValue(), count + 1)))
      .map(acc -> acc.first() / acc.second());
  }

  default One<Double> average() {
    return average(it -> it instanceof Number number ? number : null);
  }
}



