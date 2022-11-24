package lance.query.many;

import lance.func.Func;
import lance.func.tail.Average;
import lance.query.One;
import lance.Queryable;

public interface Averageable<T> extends Queryable<T> {
  default <N extends Number> One<Double> average(final Func.TryFunction<? super T, ? extends N> select) {
    return cursor().map(rec(new Average<>(select)))::keepNull;
  }

  default One<Double> average() {
    return average(it -> it instanceof Number n ? n.doubleValue() : null);
  }
}



