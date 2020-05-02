package io.artoo.query.one;

import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.impl.Or;

import java.util.function.Function;
import java.util.function.Supplier;

import static io.artoo.type.Nullability.nonNullable;

public interface Either<T extends Record> extends Queryable<T> {
  default One<T> or(final T value) {
    return or(One.just(nonNullable(value, "value")));
  }

  default <O extends One<T>> One<T> or(final O otherwise) {
    nonNullable(otherwise, "otherwise");
    return new Or<>(this, otherwise, "Inconsistent queryable.", IllegalStateException::new)::iterator;
  }

  default <E extends RuntimeException> One<T> or(final String message, final Function<? super String, ? extends E> exception) {
    nonNullable(message, "message");
    nonNullable(exception, "exception");
    return new Or<>(this, One.none(), message, exception)::iterator;
  }

  default <E extends RuntimeException> One<T> or(final Supplier<? extends E> exception) {
    nonNullable(exception, "exception");
    return or("nothing", it -> exception.get());
  }
}

