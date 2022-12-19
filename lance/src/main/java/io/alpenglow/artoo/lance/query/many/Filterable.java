package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.func.TryPredicate2;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.query.closure.NotOfType;
import io.alpenglow.artoo.lance.query.closure.OfType;
import io.alpenglow.artoo.lance.query.closure.Where;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final TryPredicate1<? super T> where) {
    return where((index, param) -> where.invoke(param));
  }

  default Many<T> where(final TryPredicate2<? super Integer, ? super T> where) {
    return () -> cursor().map(new Where<>(where));
  }

  default <R> Many<R> ofType(final Class<? extends R> type) {
    return () -> cursor().map(new OfType<>(type));
  }
  default <R> Many<T> notOfType(final Class<? extends R> type) {
    return () -> cursor().map(new NotOfType<>(type));
  }
}

