package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.operation.Sum;

import static io.artoo.lance.type.Nullability.nonNullable;

interface Summable<T> extends Queryable<T> {
  default <N extends Number> One<N> sum(final Func.Uni<? super T, ? extends N> select) {
    return One.done(() -> cursor().map(new Sum<T, N, N>(select)));
  }

  default One<T> sum() {
    return One.done(() -> cursor().map(new Sum<>(it -> it instanceof Number n ? n : null)));
  }
}

