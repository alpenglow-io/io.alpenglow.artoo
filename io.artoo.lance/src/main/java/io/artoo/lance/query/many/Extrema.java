package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.operation.Extremum;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Extrema<T> extends Queryable<T> {

  default <N extends Number> One<N> max(final Func.Uni<? super T, ? extends N> select) {
    return extreme(1, select);
  }

  default One<T> max() {
    return extreme(1);
  }

  default <N extends Number> One<N> min(final Func.Uni<? super T, ? extends N> select) {
    return extreme(-1, select);
  }

  default One<T> min() {
    return extreme(-1);
  }

  private One<T> extreme(int type) {
    return this.extreme(type, it -> it instanceof Number n ? n : null);
  }

  private <N extends Number, V> One<V> extreme(int type, final Func.Uni<? super T, ? extends N> select) {
    return () -> cursor().map(new Extremum<T, N, V>(type, nonNullable(select, "select"))).close();
  }
}

