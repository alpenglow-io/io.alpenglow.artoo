package artoo.query.impl;

import org.jetbrains.annotations.NotNull;
import artoo.func.Func;
import artoo.query.Queryable;
import artoo.query.many.Either;

import java.util.Iterator;

public final class Or<T> implements Either<T> {
  private final Queryable<T> queryable;
  private final Queryable<T> otherwise;
  private final String message;
  private final Func<? super String, ? extends RuntimeException> exception;

  public Or(final Queryable<T> queryable, final Queryable<T> otherwise, final String message, final Func<? super String, ? extends RuntimeException> exception) {
    this.queryable = queryable;
    this.otherwise = otherwise;
    this.message = message;
    this.exception = exception;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var cursor = queryable.iterator();
    if (cursor.hasNext()) return cursor;

    final var except = exception.apply(message);
    if (except == null)
      return otherwise.iterator();

    throw except;
  }
}
