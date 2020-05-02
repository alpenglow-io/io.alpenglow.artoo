package io.artoo.query.many.impl;

import io.artoo.cursor.Cursor;


import io.artoo.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public final class Unique<T> implements Queryable<T> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final boolean first;
  private final boolean single;
  private final Predicate<? super T> where;

  @Contract(pure = true)
  public Unique(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final boolean first, final boolean single, final Predicate<? super T> where) {
    this.queryable = queryable;
    this.peek = peek;
    this.first = first;
    this.single = single;
    this.where = where;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    T result = null;
    var done = false;
    var index = 0;
    for (var cursor = queryable.iterator(); cursor.hasNext() && (!first || result == null) && !done; index++) {
      var it = cursor.next();
      peek.accept(index, it);
      if (it != null && where.test(it)) {
        done = single && result != null;
        result = !single || result == null ? it : null;
      }
    }
    return Cursor.of(result);
  }
}
