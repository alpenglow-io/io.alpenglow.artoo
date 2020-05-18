package io.artoo.query.one;

import io.artoo.query.One;
import io.artoo.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.artoo.type.Nullability.nonNullable;

public interface Otherwise<T extends Record> extends Queryable<T> {
  default One<T> or(final T value) {
    return or(One.just(nonNullable(value, "value")));
  }

  default <O extends One<T>> One<T> or(final O otherwise) {
    nonNullable(otherwise, "otherwise");
    return new Or<>(this, otherwise)::iterator;
  }

  default <E extends RuntimeException> One<T> or(final String message, final Function<? super String, ? extends E> exception) {
    nonNullable(message, "message");
    nonNullable(exception, "exception");
    return new Exceptionally<>(this, message, exception)::iterator;
  }

  default <E extends RuntimeException> One<T> or(final Supplier<? extends E> exception) {
    nonNullable(exception, "exception");
    return or(null, it -> exception.get());
  }
}

final class Exceptionally<T extends Record, R extends RuntimeException> implements Otherwise<T> {
  private final Queryable<T> queryable;
  private final String message;
  private final Function<? super String, ? extends R> exception;

  Exceptionally(final Queryable<T> queryable, final String message, final Function<? super String, ? extends R> exception) {
    this.queryable = queryable;
    this.message = message;
    this.exception = exception;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var cursor = queryable.cursor();
    if (cursor.hasNext()) return cursor;

    throw exception.apply(message);
  }
}

final class Or<T extends Record, O extends One<T>> implements Otherwise<T> {
  private final Queryable<T> queryable;
  private final O otherwise;

  public Or(final Queryable<T> queryable, final O otherwise) {
    this.queryable = queryable;
    this.otherwise = otherwise;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var cursor = queryable.cursor();
    return cursor.hasNext() ? cursor : otherwise.cursor();
  }
}

