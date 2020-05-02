package io.artoo.query.many.impl;

import io.artoo.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public final class Partition<T> implements Queryable<T> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final BiPredicate<? super Integer, ? super T> where;

  @Contract(pure = true)
  public Partition(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final BiPredicate<? super Integer, ? super T> where) {
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var result = new ArrayList<T>();
    var index = 0;
    var cursor = queryable.iterator();
    var verified = false;
    if (cursor.hasNext()) {
      do
      {
        final var it = cursor.next();
        peek.accept(index, it);
        verified = it != null && where.test(index, it);
        if (verified) result.add(it);
        index++;
      } while (cursor.hasNext() && verified);
    }
    return result.iterator();
  }
}
