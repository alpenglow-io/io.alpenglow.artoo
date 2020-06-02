package io.artoo.lance.query.many;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.artoo.lance.type.Nullability.nonNullable;
import static java.util.function.Function.identity;

public interface Aggregatable<T extends Record> extends Countable<T>, Summable<T>, Averageable<T>, Extremumable<T> {
  @NotNull
  @Contract("_, _, _, _ -> new")
  default <A extends Record, R extends Record> One<A> aggregate(final A seed, final Predicate<? super T> where, final Function<? super T, ? extends R> select, final BiFunction<? super A, ? super R, ? extends A> aggregate) {
    final var whr = nonNullable(where, "where");
    final var sel = nonNullable(select, "select");
    final var agr = nonNullable(aggregate, "aggregate");
    return new Aggregate<T, A, R>(this, it -> {}, seed, whr, sel, agr)::iterator;
  }

  default <A extends Record, R extends Record> One<A> aggregate(final A seed, final Function<? super T, ? extends R> select, final BiFunction<? super A, ? super R, ? extends A> aggregate) {
    return this.aggregate(seed, it -> true, select, aggregate);
  }

  default <A extends Record, R extends Record> One<A> aggregate(final Function<? super T, ? extends R> select, final BiFunction<? super A, ? super R, ? extends A> aggregate) {
    return this.aggregate(null, it -> true, select, aggregate);
  }

  default <A extends Record> One<A> aggregate(final A seed, final BiFunction<? super A, ? super T, ? extends A> aggregate) {
    return this.aggregate(seed, it -> true, identity(), aggregate);
  }

  default One<T> aggregate(final BiFunction<? super T, ? super T, ? extends T> aggregate) {
    return this.aggregate(null, it -> true, identity(), aggregate);
  }
}

final class Aggregate<T extends Record, A extends Record, R extends Record> implements One<A> {
  private final Queryable<T> queryable;
  private final Consumer<? super T> peek;
  private final A seed;
  private final Predicate<? super T> where;
  private final Function<? super T, ? extends R> select;
  private final BiFunction<? super A, ? super R, ? extends A> aggregate;

  @Contract(pure = true)
  Aggregate(
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
