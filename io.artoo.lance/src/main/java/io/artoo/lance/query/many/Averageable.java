package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.tail.Average;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;

public interface Averageable<T> extends Queryable<T> {
  default <N extends Number> One<Double> average(final Func.MaybeFunction<? super T, ? extends N> select) {
    return cursor().map(rec(new Average<>(select)))::keepNull;
  }

  default One<Double> average() {
    return average(it -> it instanceof Number n ? n.doubleValue() : null);
  }
}



