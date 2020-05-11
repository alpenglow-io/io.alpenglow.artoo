package io.artoo.query.many.impl;


import io.artoo.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiConsumer;

// TODO: where and select are missing
public final class Insert<T extends Record, Q extends Queryable<T>> implements Queryable<T> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final Q values;

  public Insert(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final Q values) {
    this.queryable = queryable;
    this.peek = peek;
    this.values = values;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var array = new ArrayList<T>();
    var index = 0;
    for (var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      var it = cursor.next();
      peek.accept(index, it);
      if (it != null)
        array.add(it);
    }
    for (final var value : values)
      array.add(value);
    return array.iterator();
  }
}
