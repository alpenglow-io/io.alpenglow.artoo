package io.artoo.query.many.impl;

import io.artoo.query.Queryable;
import io.artoo.query.many.Projectable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public final class Select<T extends Record, R extends Record> implements Projectable<R> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final BiFunction<? super Integer, ? super T, ? extends R> select;

  @Contract(pure = true)
  public Select(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final BiFunction<? super Integer, ? super T, ? extends R> select) {
    this.queryable = queryable;
    this.peek = peek;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var array = new ArrayList<R>();
    var index = 0;
    for (var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      final var next = cursor.next();
      peek.accept(index, next);
      array.add(select.apply(index, next));
    }
    return array.iterator();
  }
}
