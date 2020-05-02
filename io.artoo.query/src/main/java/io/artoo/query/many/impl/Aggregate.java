package io.artoo.query.many.impl;

import io.artoo.cursor.Cursor;



import io.artoo.query.Queryable;
import io.artoo.query.many.Aggregatable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Aggregate<T extends Record, A extends Record, R> implements Aggregatable<A> {
  private final Queryable<T> queryable;
  private final Consumer<? super T> peek;
  private final A seed;
  private final Predicate<? super T> where;
  private final Function<? super T, ? extends R> select;
  private final BiFunction<? super A, ? super R, ? extends A> aggregate;

  @Contract(pure = true)
  public Aggregate(
    final Queryable<T> queryable,
    final Consumer<? super T> peek,
    final A seed,
    final Predicate<? super T> where,
    final Function<? super T, ? extends R> select,
    final BiFunction<? super A, ? super R, ? extends A> aggregate
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
