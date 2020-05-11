package io.artoo.query.many.impl;

import io.artoo.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

public final class Distinct<T extends Record> implements Queryable<T> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final Predicate<? super T> where;

  @Contract(pure = true)
  public Distinct(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final Predicate<? super T> where) {
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var result = new ArrayList<T>();
    var index = 0;
    for (var iterator = queryable.iterator(); iterator.hasNext(); index++) {
      var it = iterator.next();
      if (it != null)
        peek.accept(index, it);
      if (nonNull(it) && where.test(it) && !result.contains(it) || !where.test(it)) {
        result.add(it);
      }
    }
    return result.iterator();
  }
}
