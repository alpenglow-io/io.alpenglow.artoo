package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;

public interface Extremable<T> extends Queryable<T> {
  default <N extends Number> One<N> max(final Func.Uni<? super T, ? extends N> select) {
    return One.done(cursor().extreme(1, select));
  }

  default One<T> max() {
    return One.done(cursor().extreme(1));
  }

  default <N extends Number> One<N> min(final Func.Uni<? super T, ? extends N> select) {
    return One.done(cursor().extreme(-1, select));
  }

  default One<T> min() {
    return One.done(cursor().extreme(-1));
  }
}

