package io.artoo.query.many.impl;



import io.artoo.query.Queryable;
import io.artoo.query.many.Projectable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public final class SelectMany<T extends Record, R extends Record, Q extends Queryable<R>> implements Projectable<R> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final BiFunction<? super Integer, ? super T, ? extends Q> select;

  @Contract(pure = true)
  public SelectMany(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final BiFunction<? super Integer, ? super T, ? extends Q> select) {
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
      var it = cursor.next();
      if (it != null) {
        peek.accept(index, it);
        final var queried = select.apply(index, it);
        if (queried != null) {
          for (final var selected : queried) {
            if (selected != null)
              array.add(selected);
          }
        }
      }
    }
    return array.iterator();
  }
}
