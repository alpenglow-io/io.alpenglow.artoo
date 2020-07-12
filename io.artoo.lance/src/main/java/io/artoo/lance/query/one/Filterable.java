package io.artoo.lance.query.one;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.operation.OfType;
import io.artoo.lance.query.operation.Where;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Filterable<T> extends Queryable<T> {
  default One<T> where(final Pred.Uni<? super T> where) {
    final var w = nonNullable(where, "where");
    return () -> cursor().map(new Where<>((i, it) -> w.tryTest(it), (i, it) -> it));
  }

  default <R> One<R> ofType(final Class<R> type) {
    final var t = nonNullable(type, "type");
    return () -> cursor().map(new OfType<>(t));
  }
}
