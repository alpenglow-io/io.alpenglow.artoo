package io.alpenglow.artoo.lance.query.one;

import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.func.TrySupplier1;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;

public interface Elseable<ELEMENT> extends Queryable<ELEMENT> {
  default One<ELEMENT> or(final ELEMENT element) {
    return or(One.of(element));
  }

  default <OTHER extends One<ELEMENT>> One<ELEMENT> or(final OTHER otherwise) {
    return () -> cursor().or(otherwise::cursor);
  }

  default <E extends RuntimeException> One<ELEMENT> or(final String message, final TryFunction2<? super Throwable, ? super String, ? extends E> exception) {
    return () -> cursor().or(message, (m, c) -> exception.tryApply(c, m));
  }

  default <E extends RuntimeException> One<ELEMENT> or(final String message, final TryFunction1<? super String, ? extends E> exception) {
    return () -> cursor().or(message, (msg, cause) -> exception.tryApply(msg));
  }

  default <E extends RuntimeException> One<ELEMENT> or(final TrySupplier1<? extends E> exception) {
    return or(null, (it, throwable) -> exception.tryGet());
  }

  default ELEMENT otherwise(final ELEMENT other) {
    return or(other).iterator().next();
  }

  default <E extends RuntimeException> ELEMENT otherwise(final String message, final TryFunction2<? super Throwable, ? super String, ? extends E> exception) {
    return or(message, exception).iterator().next();
  }

  default <E extends RuntimeException> ELEMENT otherwise(final TrySupplier1<? extends E> exception) {
    return or(exception).iterator().next();
  }
}
