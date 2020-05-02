package io.artoo.query.one.impl;

import io.artoo.cursor.Cursor;

import io.artoo.query.Queryable;
import io.artoo.query.one.Projectable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

public final class SelectOne<T, R, Q extends Queryable<R>> implements Projectable<R> {
  private final Queryable<T> queryable;
  private final Function<? super T, ? extends Q> select;

  @Contract(pure = true)
  public SelectOne(final Queryable<T> queryable, final Function<? super T, ? extends Q> select) {
    this.queryable = queryable;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    Q result = null;
    for (final var value : queryable) {
      if (value != null) {
        result = select.apply(value);
      }
    }
    return result != null ? result.iterator() : Cursor.none();
  }
}
