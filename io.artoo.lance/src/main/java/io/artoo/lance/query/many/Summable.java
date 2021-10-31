package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.Sum;

public interface Summable<T> extends Queryable<T> {
  default <N extends Number> One<N> sum(final Func.MaybeFunction<? super T, ? extends N> select) {
    return () -> cursor().map(new Sum<T, N, N>(select)).keepNull();
  }

  default One<T> sum() {
    return () -> cursor().map(new Sum<T, Number, T>(it -> it instanceof Number n ? n : null)).keepNull();
  }
}

