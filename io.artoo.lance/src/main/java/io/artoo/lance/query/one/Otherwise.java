package io.artoo.lance.query.one;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Otherwise<T> extends Queryable<T> {
  default One<T> or(final T element) {
    return or(One.lone(nonNullable(element, "element")));
  }

  default <O extends One<T>> One<T> or(final O otherwise) {
    nonNullable(otherwise, "otherwise");
    return new Or<>(this, otherwise);
  }

  default <E extends RuntimeException> One<T> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    nonNullable(message, "message");
    nonNullable(exception, "exception");
    return new Er<>(this, message, exception);
  }

  default <E extends RuntimeException> One<T> or(final Supplier<? extends E> exception) {
    nonNullable(exception, "exception");
    return or(null, it -> exception.get());
  }
}

final class Er<T, R extends RuntimeException> implements One<T> {
  private final Queryable<T> queryable;
  private final String message;
  private final Func.Uni<? super String, ? extends R> exception;

  Er(final Queryable<T> queryable, final String message, final Func.Uni<? super String, ? extends R> exception) {
    this.queryable = queryable;
    this.message = message;
    this.exception = exception;
  }

  @NotNull
  @Override
  public final Cursor<T> cursor() {
    final var cursor = queryable.cursor();
    if (cursor.hasNext()) return cursor;

    throw exception.apply(message);
  }
}

final class Or<T, O extends One<T>> implements One<T> {
  private final Queryable<T> queryable;
  private final O otherwise;

  public Or(final Queryable<T> queryable, final O otherwise) {
    this.queryable = queryable;
    this.otherwise = otherwise;
  }

  @NotNull
  @Override
  public final Cursor<T> cursor() {
    final var cursor = queryable.cursor();
    return cursor.hasNext() ? cursor : otherwise.cursor();
  }
}

