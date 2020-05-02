package io.artoo.query.one.impl;

import io.artoo.cursor.Cursor;

import io.artoo.query.Queryable;
import io.artoo.query.many.Projectable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

public final class Select<T extends Record, R extends Record> implements Projectable<R> {
  private final Queryable<T> queryable;
  private final Function<? super T, ? extends R> select;

  @Contract(pure = true)
  public Select(final Queryable<T> queryable, final Function<? super T, ? extends R> select) {
    this.queryable = queryable;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    R result = null;
    for (final var value : queryable) if (value != null) result = select.apply(value);
    return Cursor.of(result);
  }
}
