package io.alpenglow.artoo.lance.query.one;

import io.alpenglow.artoo.lance.func.TryPredicate;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.NotOfType;
import io.alpenglow.artoo.lance.query.func.OfType;
import io.alpenglow.artoo.lance.query.func.Where;

public interface Filterable<T> extends Queryable<T> {
  default One<T> where(final TryPredicate<? super T> where) {
    return () -> cursor().map(new Where<>((i, it) -> where.tryTest(it)));
  }

  default <R> One<R> ofType(final Class<? extends R> type) {
    return () -> cursor().map(new OfType<>(type));
  }

  default <R> One<T> notOfType(final Class<? extends R> type) {
    return () -> cursor().map(new NotOfType<>(type));
  }
}
