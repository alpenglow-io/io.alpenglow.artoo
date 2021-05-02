package io.artoo.lance.query.one;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;

public interface Elseable<T> extends Queryable<T> {
  default One<T> or(final T element) {
    return or(One.maybe(element));
  }

  default <O extends One<T>> One<T> or(final O otherwise) {
    return () -> cursor().or(otherwise::cursor);
  }

  default <E extends RuntimeException> One<T> or(final String message, final Func.Bi<? super Throwable, ? super String, ? extends E> exception) {
    return () -> cursor().or(message, (m, c) -> exception.tryApply(c, m));
  }

  default <E extends RuntimeException> One<T> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    return () -> cursor().or(message, (msg, cause) -> exception.tryApply(msg));
  }

  default <E extends RuntimeException> One<T> or(final Suppl.Uni<? extends E> exception) {
    return or(null, (it, throwable) -> exception.tryGet());
  }

  default T otherwise(final T other) {
    return or(other).iterator().next();
  }

  default <E extends RuntimeException> T otherwise(final String message, final Func.Bi<? super Throwable, ? super String, ? extends E> exception) {
    return or(message, exception).iterator().next();
  }

  default <E extends RuntimeException> T otherwise(String message, Func.Uni<? super String, ? extends E> exception) {
    return or(message, exception).iterator().next();
  }

  default <E extends RuntimeException> T otherwise(final Suppl.Uni<? extends E> exception) {
    return or(exception).iterator().next();
  }
}
