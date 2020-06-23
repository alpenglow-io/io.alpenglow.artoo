package io.artoo.lance.query.one;

import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Otherwise<T> extends Queryable<T> {
  default One<T> or(final T value) {
    return or(One.lone(nonNullable(value, "element")));
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

final class Exceptionally<T, R extends RuntimeException> implements Otherwise<T> {
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
    final var cursor = queryable.iterator();
    if (cursor.hasNext()) return cursor;

    throw exception.apply(message);
  }
}

final class Or<T, O extends One<T>> implements Otherwise<T> {
  private final Queryable<T> queryable;
  private final O otherwise;

  public Or(final Queryable<T> queryable, final O otherwise) {
    this.queryable = queryable;
    this.otherwise = otherwise;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var cursor = queryable.iterator();
    return cursor.hasNext() ? cursor : otherwise.iterator();
  }
}

