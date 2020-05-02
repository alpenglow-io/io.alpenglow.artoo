package io.artoo.query.many.impl;

import io.artoo.cursor.Cursor;


import io.artoo.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class Extremum<T, R> implements Queryable<R> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final int extreme;
  private final Function<? super R, Comparable<? super R>> comparing;
  private final Function<? super T, ? extends R> select;

  @Contract(pure = true)
  public Extremum(
    final Queryable<T> queryable,
    final BiConsumer<? super Integer, ? super T> peek,
    final int extreme,
    final Function<? super R, Comparable<? super R>> comparing,
    final Function<? super T, ? extends R> select
  ) {
    this.queryable = queryable;
    this.peek = peek;
    this.extreme = extreme;
    this.comparing = comparing;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    R result = null;
    var index = 0;
    for (var iterator = queryable.iterator(); iterator.hasNext(); index++) {
      var it = iterator.next();
      peek.accept(index, it);
      if (it != null) {
        final var mapped = select.apply(it);
        if (result == null || mapped != null && comparing.apply(mapped).compareTo(result) == extreme)
          result = mapped;
      }
    }
    return Cursor.of(result);
  }
}
