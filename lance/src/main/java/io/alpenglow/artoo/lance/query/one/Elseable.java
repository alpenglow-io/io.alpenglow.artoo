package io.alpenglow.artoo.lance.query.one;

import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.func.TrySupplier1;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;

public interface Elseable<T> extends Queryable<T> {
  default One<T> or(final T element) {
    return or(One.maybe(element));
  }

  default <O extends One<T>> One<T> or(final O otherwise) {
    return () -> cursor().or(otherwise::cursor);
  }

  default <E extends RuntimeException> One<T> or(final String message, final TryFunction2<? super Throwable, ? super String, ? extends E> exception) {
    return () -> cursor().or(message, (m, c) -> exception.tryApply(c, m));
  }

  default <E extends RuntimeException> One<T> or(final String message, final TryFunction1<? super String, ? extends E> exception) {
    return () -> cursor().or(message, (msg, cause) -> exception.tryApply(msg));
  }

  default <E extends RuntimeException> One<T> or(final TrySupplier1<? extends E> exception) {
    return or(null, (it, throwable) -> exception.tryGet());
  }

  default T otherwise(final T other) {
    return or(other).iterator().next();
  }

  default <E extends RuntimeException> T otherwise(final String message, final TryFunction2<? super Throwable, ? super String, ? extends E> exception) {
    return or(message, exception).iterator().next();
  }

  default <E extends RuntimeException> T otherwise(final TrySupplier1<? extends E> exception) {
    return or(exception).iterator().next();
  }
}
