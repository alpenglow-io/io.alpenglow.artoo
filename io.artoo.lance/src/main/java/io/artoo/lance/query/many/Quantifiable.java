package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.query.operation.All;
import io.artoo.lance.query.operation.Any;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Quantifiable<T> extends Queryable<T> {
  default <R> One<Boolean> all(final Class<R> type) {
    return all(type::isInstance);
  }

  default One<Boolean> all(final Pred.Uni<? super T> where) {
    final var w = nonNullable(where, "where");
    return () -> cursor().map(new All<>(w)).end();
  }

  default One<Boolean> any() { return this.any(t -> true); }

  default One<Boolean> any(final Pred.Uni<? super T> where) {
    final var w = nonNullable(where, "where");
    return () -> cursor().map(new Any<>(w)).end().or(() -> Cursor.just(false));
  }
}

