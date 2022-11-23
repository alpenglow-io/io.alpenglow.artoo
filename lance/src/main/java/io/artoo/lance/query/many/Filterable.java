package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.NotOfType;
import io.artoo.lance.query.func.OfType;
import io.artoo.lance.query.func.Where;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final Pred.MaybePredicate<? super T> where) {
    return where((index, param) -> where.tryTest(param));
  }

  default Many<T> where(final Pred.Bi<? super Integer, ? super T> where) {
    return () -> cursor().map(new Where<>(where));
  }

  default <R> Many<R> ofType(final Class<? extends R> type) {
    return () -> cursor().map(new OfType<>(type));
  }

  default <R> Many<T> notOfType(final Class<? extends R> type) {
    return () -> cursor().map(new NotOfType<>(type));
  }
}

