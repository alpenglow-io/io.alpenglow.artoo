package io.artoo.lance.query.one;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Otherwise<T> extends Queryable<T> {
  default One<T> or(final T element) {
    return or(One.of(element));
  }

  default <O extends One<T>> One<T> or(final O otherwise) {
    return () -> cursor().or(otherwise::cursor);
  }

  default <E extends RuntimeException> One<T> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    return () -> cursor().or(message, exception);
  }

  default <E extends RuntimeException> One<T> or(final Suppl.Uni<? extends E> exception) {
    return or(null, it -> exception.tryGet());
  }
}
