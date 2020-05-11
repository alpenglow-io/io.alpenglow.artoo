package io.artoo.query.many;

import io.artoo.query.Many;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Or;

import java.util.function.Function;
import java.util.function.Supplier;

import static io.artoo.type.Nullability.nonNullable;

public interface Otherwise<T extends Record> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> or(final T... values) {
    nonNullable(values, "values");
    return or(Many.from(values));
  }

  default Many<T> or(final Many<T> many) {
    nonNullable(many, "many");
    return new Or<>(this, many, "Inconsistent queryable.", IllegalStateException::new)::iterator;
  }

  default <E extends RuntimeException> Many<T> or(final String message, final Function<? super String, ? extends E> exception) {
    nonNullable(message, "message");
    nonNullable(exception, "exception");
    return new Or<>(this, Many.none(), message, exception)::iterator;
  }

  default <E extends RuntimeException> Many<T> or(final Supplier<? extends E> exception) {
    nonNullable(exception, "exception");
    return or("nothing", it -> exception.get());
  }
}
