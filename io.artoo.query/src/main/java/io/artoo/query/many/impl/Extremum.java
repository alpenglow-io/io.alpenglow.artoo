package io.artoo.query.many.impl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import io.artoo.cursor.Cursor;
import io.artoo.func.$2.ConsInt;
import io.artoo.func.Func;
import io.artoo.query.Queryable;

import java.util.Iterator;

public final class Extremum<T, R> implements Queryable<R> {
  private final Queryable<T> queryable;
  private final ConsInt<? super T> peek;
  private final int extreme;
  private final Func<? super R, Comparable<? super R>> comparing;
  private final Func<? super T, ? extends R> select;

  @Contract(pure = true)
  public Extremum(
    final Queryable<T> queryable,
    final ConsInt<? super T> peek,
    final int extreme,
    final Func<? super R, Comparable<? super R>> comparing,
    final Func<? super T, ? extends R> select
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
      peek.acceptInt(index, it);
      if (it != null) {
        final var mapped = select.apply(it);
        if (result == null || mapped != null && comparing.apply(mapped).compareTo(result) == extreme)
          result = mapped;
      }
    }
    return Cursor.of(result);
  }
}
