package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;

public interface
Averageable<T> extends Queryable<T> {
  default <N extends Number> One<Double> average(final Func.Uni<? super T, ? extends N> select) {
    return One.done(cursor().average(select));
  }

  default One<Double> average() {
    return One.done(cursor().average());
  }
}



