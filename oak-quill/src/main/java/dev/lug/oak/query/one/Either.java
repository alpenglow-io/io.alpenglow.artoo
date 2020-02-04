package dev.lug.oak.query.one;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.query.One;
import dev.lug.oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.lug.oak.type.Nullability.*;
import static java.util.Objects.requireNonNullElse;

public interface Either<T> extends Queryable<T> {
  default One<T> or(final T value) {
    return new Or<>(this, nonNullable(value, "value"));
  }
  default <E extends RuntimeException> One<T> or(final String message, final Function1<? super String, ? extends E> exception) {
    return new OrException<>(this, nonNullable(message, "message"), nonNullable(exception, "exception"));
  }
}

final class Or<T> implements One<T> {
  private final Queryable<T> queryable;
  private final T otherwise;

  @Contract(pure = true)
  Or(final Queryable<T> queryable, final T otherwise) {
    this.queryable = queryable;
    this.otherwise = otherwise;
  }

  @Override
  @Contract(pure = true)
  public final Iterator<T> iterator() {
    return Cursor.once(requireNonNullElse(queryable.iterator().next(), otherwise));
  }
}

final class OrException<T, E extends RuntimeException> implements One<T> {
  private final Queryable<T> queryable;
  private final String message;
  private final Function1<? super String, ? extends E> exception;

  @Contract(pure = true)
  OrException(final Queryable<T> queryable, final String message, final Function1<? super String, ? extends E> exception) {
    this.queryable = queryable;
    this.message = message;
    this.exception = exception;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var value = queryable.iterator().next();
    if (value == null) throw exception.apply(message);
    return Cursor.once(value);
  }
}
