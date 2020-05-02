package io.artoo.query.impl;

import io.artoo.query.Queryable;
import io.artoo.query.many.Either;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

public final class Or<R extends Record> implements Either<R> {
  private final Queryable<R> queryable;
  private final Queryable<R> otherwise;
  private final String message;
  private final Function<? super String, ? extends RuntimeException> exception;

  public Or(final Queryable<R> queryable, final Queryable<R> otherwise, final String message, final Function<? super String, ? extends RuntimeException> exception) {
    this.queryable = queryable;
    this.otherwise = otherwise;
    this.message = message;
    this.exception = exception;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var cursor = queryable.iterator();
    if (cursor.hasNext()) return cursor;

    final var except = exception.apply(message);
    if (except == null)
      return otherwise.iterator();

    throw except;
  }
}
