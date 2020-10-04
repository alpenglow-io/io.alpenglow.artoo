package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.oper.Average;

public interface Averageable<T> extends Queryable<T> {
  default <N extends Number> One<Double> average(final Func.Uni<? super T, ? extends N> select) {
    return () -> cursor().map(new Average<>(select));
  }

  default One<Double> average() {
    return average(it -> it instanceof Number n ? n.doubleValue() : null);
  }
}



