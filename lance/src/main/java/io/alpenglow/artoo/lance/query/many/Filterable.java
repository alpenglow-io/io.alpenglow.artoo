package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.func.TryBiPredicate;
import io.alpenglow.artoo.lance.func.TryPredicate;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.NotOfType;
import io.alpenglow.artoo.lance.query.func.OfType;
import io.alpenglow.artoo.lance.query.func.Where;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final TryPredicate<? super T> where) {
    return where((index, param) -> where.tryTest(param));
  }

  default Many<T> where(final TryBiPredicate<? super Integer, ? super T> where) {
    return () -> cursor().map(new Where<>(where));
  }

  default <R> Many<R> ofType(final Class<? extends R> type) {
    return () -> cursor().map(new OfType<>(type));
  }

  default <R> Many<T> notOfType(final Class<? extends R> type) {
    return () -> cursor().map(new NotOfType<>(type));
  }
}

