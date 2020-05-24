package io.artoo.query.many;

import io.artoo.cursor.Cursor;
import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.value.Int32;
import io.artoo.value.Numeral;
import io.artoo.value.Single64;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static io.artoo.type.Nullability.nonNullable;
import static io.artoo.type.Numeric.asNumber;

public interface Averageable<T extends Record> extends Queryable<T> {
  default <V extends Number, N extends Number, R extends Record & Numeral<N, R>> One<R> average(final BiFunction<? super Integer, ? super T, ? extends V> select, final Function<? super V, ? extends R> asNumber) {
    final var s = nonNullable(select, "select");
    final var n = nonNullable(asNumber, "asNumber");
    return new Average<T, V, N, R>(this, (i, it) -> {}, (i, it) -> true, s, n)::iterator;
  }

  default <V extends Number, N extends Number, R extends Record & Numeral<N, R>> One<R> average(final Function<? super T, ? extends V> select, final Function<? super V, ? extends R> asNumeral) {
    final var s = nonNullable(select, "select");
    final var n = nonNullable(asNumeral, "asNumeral");
    return this.<V, N, R>average((index, it) -> s.apply(it), n);
  }

  default <R extends Number> One<Single64> average(final Function<? super T, ? extends R> select) {
    final var s = nonNullable(select, "select");
    return new Average<>(this, (i, it) -> {}, (i, it) -> true, (i, it) -> s.apply(it), Single64::let)::iterator;
  }

  @SuppressWarnings("rawtypes")
  default One<Single64> average() {
    return new Average<>(this, (i, it) -> {}, (i, it) -> it instanceof Numeral, (i, it) -> ((Numeral) it).box().doubleValue(), Single64::let)::iterator;
  }
}

final class Average<T extends Record, V extends Number, N extends Number, R extends Record & Numeral<N, R>> implements Averageable<R> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final BiPredicate<? super Integer, ? super T> where;
  private final BiFunction<? super Integer, ? super T, ? extends V> select;
  private final Function<? super V, ? extends R> asNumber;

  @Contract(pure = true)
  public Average(
    final Queryable<T> queryable,
    final BiConsumer<? super Integer, ? super T> peek,
    final BiPredicate<? super Integer, ? super T> where,
    final BiFunction<? super Integer, ? super T, ? extends V> select,
    final Function<? super V, ? extends R> asNumber
  ) {
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
    this.select = select;
    this.asNumber = asNumber;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    R total = null;
    var count = 0;
    var index = 0;
    for (var iterator = queryable.iterator(); iterator.hasNext(); index++) {
      var next = iterator.next();
      peek.accept(index, next);
      if (next != null && where.test(index, next)) {
        final var value = select.andThen(asNumber).apply(index, next);
        if (value != null) {
          total = Numeral.add(total, value);
          count++;
        }
      }
    }
    return count == 0 ? Cursor.none() : Cursor.of(total.div(Int32.let(count)));
  }
}



