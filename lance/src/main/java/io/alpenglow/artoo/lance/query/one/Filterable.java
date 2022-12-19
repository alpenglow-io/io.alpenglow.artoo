package io.alpenglow.artoo.lance.query.one;

import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.query.closure.NotOfType;
import io.alpenglow.artoo.lance.query.closure.OfType;
import io.alpenglow.artoo.lance.query.closure.Where;

public interface Filterable<T> extends Queryable<T> {
  default One<T> where(final TryPredicate1<? super T> where) {
    return () -> cursor().map(new Where<>((i, it) -> where.invoke(it)));
  }

  default <R> One<R> ofType(final Class<? extends R> type) {
    return () -> cursor().map(new OfType<>(type));
  }

  default <R> One<T> notOfType(final Class<? extends R> type) {
    return () -> cursor().map(new NotOfType<>(type));
  }
}
