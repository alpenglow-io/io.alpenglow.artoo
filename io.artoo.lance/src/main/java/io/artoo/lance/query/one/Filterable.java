package io.artoo.lance.query.one;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;

public interface Filterable<T> extends Queryable<T> {
  default One<T> where(final Pred.Uni<? super T> where) {
    return One.done(cursor().where(where));
  }

  default <R> One<R> ofType(final Class<? extends R> type) {
    return One.done(cursor().ofType(type));
  }
}
