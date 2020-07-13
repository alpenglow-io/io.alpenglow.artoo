package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.operation.OfType;
import io.artoo.lance.query.operation.Where;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final Pred.Uni<? super T> where) {
    nonNullable(where, "where");
    return where((index, param) -> where.tryTest(param));
  }

  default Many<T> where(final Pred.Bi<? super Integer, ? super T> where) {
    return where(where, (i, it) -> it);
  }

  default <R> Many<R> ofType(final Class<? extends R> type) {
    final var t = nonNullable(type, "type");
    return () -> cursor().map(new OfType<>(t).butNulls());
  }

  default <R> Many<R> where(final Pred.Bi<? super Integer, ? super T> where, final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    final var w = nonNullable(where, "where");
    final var s = nonNullable(select, "select");
    return () -> cursor().map(new Where<>(w, s).butNulls());
  }
}

