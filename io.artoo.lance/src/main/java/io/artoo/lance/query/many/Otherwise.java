package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Otherwise<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> or(final T... values) {
    nonNullable(values, "values");
    return or(Many.from(values));
  }

  default Many<T> or(final Many<T> many) {
    nonNullable(many, "many");
    return new Or<>(this, many, "Inconsistent queryable.", IllegalStateException::new);
  }

  default <E extends RuntimeException> Many<T> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    nonNullable(message, "message");
    nonNullable(exception, "exception");
    return new Or<>(this, Many.none(), message, exception);
  }

  default <E extends RuntimeException> Many<T> or(final Supplier<? extends E> exception) {
    nonNullable(exception, "exception");
    return or("nothing", it -> exception.get());
  }
}

final class Or<R> implements Many<R> {
  private final Queryable<R> queryable;
  private final Queryable<R> otherwise;
  private final String message;
  private final Func.Uni<? super String, ? extends RuntimeException> exception;

  Or(final Queryable<R> queryable, final Queryable<R> otherwise, final String message, final Func.Uni<? super String, ? extends RuntimeException> exception) {
    this.queryable = queryable;
    this.otherwise = otherwise;
    this.message = message;
    this.exception = exception;
  }

  @NotNull
  @Override
  public final Cursor<R> cursor() {
    final var cursor = queryable.cursor();
    if (cursor.hasNext()) return cursor;

    final var except = exception.apply(message);
    if (except == null)
      return Cursor.from(otherwise.iterator());

    throw except;
  }
}
