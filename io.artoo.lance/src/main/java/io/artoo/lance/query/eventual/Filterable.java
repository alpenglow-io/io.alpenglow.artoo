package io.artoo.lance.query.eventual;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Eventual;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.query.operation.Select.as;
import static io.artoo.lance.query.operation.Where.on;

public interface Filterable<T> extends Queryable<T> {
  default Eventual<T> where(final Pred.Uni<? super T> where) {
    return () -> cursor().map(on(where));
  }

  default <R> Eventual<R> ofType(final Class<? extends R> type) {
    return () -> cursor().map(on(type)).map(as(type));
  }
}
