package io.artoo.query.many.impl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import io.artoo.cursor.Cursor;
import io.artoo.func.$2.Func;
import io.artoo.func.Cons;
import io.artoo.func.Pred;
import io.artoo.query.Queryable;

import java.util.Iterator;

public final class Aggregate<T, A, R> implements Queryable<A> {
  private final Queryable<T> queryable;
  private final Cons<? super T> peek;
  private final A seed;
  private final Pred<? super T> where;
  private final io.artoo.func.Func<? super T, ? extends R> select;
  private final Func<? super A, ? super R, ? extends A> aggregate;

  @Contract(pure = true)
  public Aggregate(
    final Queryable<T> queryable,
    final Cons<? super T> peek,
    final A seed,
    final Pred<? super T> where,
    final io.artoo.func.Func<? super T, ? extends R> select,
    final Func<? super A, ? super R, ? extends A> aggregate
  ) {
    this.queryable = queryable;
    this.peek = peek;
    this.seed = seed;
    this.where = where;
    this.select = select;
    this.aggregate = aggregate;
  }

  @NotNull
  @Override
  public final Iterator<A> iterator() {
    var aggregated = seed;
    for (final var it : queryable) {
      if (peek != null) peek.accept(it);
      if (where.test(it)) {
        final var selected = select.apply(it);
        if (selected != null) {
          aggregated = aggregate.apply(aggregated, selected);
        }
      }
    }
    return Cursor.of(aggregated);
  }
}
