package io.artoo.lance.query.many;

import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Otherwise<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> or(final T... values) {
    return () -> cursor().or(() -> Cursor.open(values));
  }

  default Many<T> or(final Many<T> many) {
    return () -> cursor().or(many::cursor);
  }

  default <E extends RuntimeException> Many<T> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    return () -> cursor().or(message, exception);
  }

  default <E extends RuntimeException> Many<T> or(final Suppl.Uni<? extends E> exception) {
    return () -> cursor().or(null, it -> exception.tryGet());
  }
}

