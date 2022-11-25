package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.func.TryBiFunction;
import io.alpenglow.artoo.lance.func.TrySupplier;
import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.Queryable;

@SuppressWarnings("unchecked")
public interface Elseable<T> extends Queryable<T> {
  default Many<T> or(final T... values) {
    return () -> cursor().or(() -> Cursor.open(values));
  }

  default Many<T> or(final Many<T> many) {
    return () -> cursor().or(many::cursor);
  }

  default <E extends RuntimeException> Many<T> or(final String message, final TryBiFunction<? super String, ? super Throwable, ? extends E> exception) {
    return () -> cursor().or(message, exception);
  }

  default <E extends RuntimeException> Many<T> or(final TrySupplier<? extends E> exception) {
    return () -> cursor().or(null, (it, throwable) -> exception.tryGet());
  }
}

