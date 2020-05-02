package io.artoo.query.many.impl;

import io.artoo.cursor.Cursor;
import io.artoo.query.Many;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@SuppressWarnings("ALL")
public final class Array<T> implements Many<T> {
  private final T[] values;

  @SafeVarargs
  @Contract(pure = true)
  public Array(final T... values) {
    this.values = values;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return Cursor.many(values);
  }

  private record Entry(
    Entry prev,
    Entry next,
    Object value) {
  }
}
