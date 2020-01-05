package dev.lug.oak.quill.single;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.quill.Structable;
import dev.lug.oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.lug.oak.type.Nullability.*;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElse;

public interface Casing<T> extends Structable<T> {
  default One<T> or(final T value) {
    return new Either<>(this, nonNullable(value, "value"));
  }
  default <E extends RuntimeException> One<T> or(final String message, final Function1<? super String, ? extends E> exception) {
    return new EitherException<>(this, nonNullable(message, "message"), nonNullable(exception, "exception"));
  }
}

final class Either<T> implements One<T> {
  private final Structable<T> structable;
  private final T otherwise;

  @Contract(pure = true)
  Either(final Structable<T> structable, final T otherwise) {
    this.structable = structable;
    this.otherwise = otherwise;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return Cursor.of(requireNonNullElse(structable.iterator().next(), otherwise));
  }
}

final class EitherException<T, E extends RuntimeException> implements One<T> {
  private final Structable<T> structable;
  private final String message;
  private final Function1<? super String, ? extends E> exception;

  @Contract(pure = true)
  EitherException(final Structable<T> structable, final String message, final Function1<? super String, ? extends E> exception) {
    this.structable = structable;
    this.message = message;
    this.exception = exception;
  }

  @NotNull
  @Override
  public Iterator<T> iterator() {
    final var value = structable.iterator().next();
    if (isNull(value)) throw exception.apply(message);
    return Cursor.of(value);
  }
}
