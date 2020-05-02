package io.artoo.query.one.impl;

import io.artoo.cursor.Cursor;


import io.artoo.query.Queryable;
import io.artoo.query.one.Filterable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Where<T, R> implements Filterable<R> {
  private final Queryable<T> queryable;
  private final Predicate<? super T> where;
  private final Function<? super T, ? extends R> select;

  @Contract(pure = true)
  public Where(final Queryable<T> queryable, final Predicate<? super T> where, final Function<? super T, ? extends R> select) {
    this.queryable = queryable;
    this.where = where;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    R result = null;
    for (final var value : queryable) {
      if (value != null && where.test(value)) {
        result = select.apply(value);
      }
    }
    return Cursor.of(result);
  }
}
