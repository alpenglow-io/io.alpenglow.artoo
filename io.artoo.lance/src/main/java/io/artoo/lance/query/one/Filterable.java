package io.artoo.lance.query.one;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.query.operation.Select.as;
import static io.artoo.lance.query.operation.Where.on;
import static io.artoo.lance.type.Nullability.nonNullable;

public interface Filterable<T> extends Queryable<T> {
  default One<T> where(final Pred.Uni<? super T> where) {
    return () -> cursor().map(on(where));
  }

  default <R> One<R> ofType(final Class<? extends R> type) {
    return () -> cursor().map(on(type)).map(as(type));
  }
}
