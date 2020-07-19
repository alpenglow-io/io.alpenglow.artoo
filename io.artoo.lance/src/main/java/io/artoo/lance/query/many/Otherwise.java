package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Otherwise<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> or(final T... values) {
    nonNullable(values, "values");
    return or(Many.from(values));
  }

  default Many<T> or(final Many<T> many) {
    nonNullable(many, "many");
    return () -> cursor().or(many::cursor);
  }

  default <E extends RuntimeException> Many<T> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    final var m = nonNullable(message, "message");
    final var e = nonNullable(exception, "exception");
    return () -> cursor().or(m, e);
  }

  default <E extends RuntimeException> Many<T> or(final Suppl.Uni<? extends E> exception) {
    nonNullable(exception, "exception");
    return or("nothing", it -> exception.tryGet());
  }
}

