package io.artoo.query.one.impl;

import io.artoo.cursor.Cursor;
import io.artoo.func.Func;
import io.artoo.func.Pred;
import io.artoo.query.Queryable;
import io.artoo.query.one.Filterable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public final class Where<T, R> implements Filterable<R> {
  private final Queryable<T> queryable;
  private final Pred<? super T> where;
  private final Func<? super T, ? extends R> select;

  @Contract(pure = true)
  public Where(final Queryable<T> queryable, final Pred<? super T> where, final Func<? super T, ? extends R> select) {
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
