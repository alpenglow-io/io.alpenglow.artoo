package io.alpenglow.artoo.lance.query.one.oftwo;

import io.alpenglow.artoo.lance.func.TryBiFunction;
import io.alpenglow.artoo.lance.func.TrySupplier;
import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.tuple.Pair;

public interface Otherwise<A, B> extends Queryable.OfTwo<A, B> {
  @SuppressWarnings("unchecked")
  default <P extends Pair<A, B>> One.OfTwo<A, B> or(final P... values) {
    return () -> cursor().or(() -> Cursor.open(values));
  }

  default One.OfTwo<A, B> or(final One.OfTwo<A, B> many) {
    return () -> cursor().or(many::cursor);
  }

  default <E extends RuntimeException> One.OfTwo<A, B> or(final String message, final TryBiFunction<? super String, ? super Throwable, ? extends E> exception) {
    return () -> cursor().or(message, exception);
  }

  default <E extends RuntimeException> One.OfTwo<A, B> or(final TrySupplier<? extends E> exception) {
    return () -> cursor().or(null, (it, throwable) -> exception.tryGet());
  }
}

