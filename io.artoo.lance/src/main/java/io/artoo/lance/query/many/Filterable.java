package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.query.operation.Index.indexed;
import static io.artoo.lance.query.operation.Select.as;
import static io.artoo.lance.query.operation.Where.on;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final Pred.Uni<? super T> where) {
    return Many.wany(cursor().where(where));
  }

  default Many<T> where(final Pred.Bi<? super Integer, ? super T> where) {
    return Many.wany(cursor().where(where));
  }

  default <R> Many<R> ofType(final Class<? extends R> type) {
    return Many.wany(cursor().ofType(type));
  }
}

