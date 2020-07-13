package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.operation.Average;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface
Averageable<T> extends Queryable<T> {
  default <N extends Number> One<Double> average(final Func.Uni<? super T, ? extends N> select) {
    return () -> cursor().map(new Average<>(nonNullable(select, "select"))).end();
  }

  default One<Double> average() {
    return this.average(it -> it instanceof Number n ? n.doubleValue() : null);
  }
}



