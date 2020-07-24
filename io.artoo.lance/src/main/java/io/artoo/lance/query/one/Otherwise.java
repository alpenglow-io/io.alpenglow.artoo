package io.artoo.lance.query.one;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Otherwise<T> extends Queryable<T> {
  default One<T> or(final T element) {
    return or(One.of(element));
  }

  default <O extends One<T>> One<T> or(final O otherwise) {
    final var o = nonNullable(otherwise, "otherwise");
    return () -> cursor().or(o::cursor);
  }

  default <E extends RuntimeException> One<T> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    final var m = nonNullable(message, "message");
    final var e = nonNullable(exception, "exception");
    return () -> cursor().or(m, e);
  }

  default <E extends RuntimeException> One<T> or(final Suppl.Uni<? extends E> exception) {
    final var e = nonNullable(exception, "exception");
    return or(null, it -> e.tryGet());
  }
}
