package io.artoor.query.many;

import io.artoor.cursor.Cursor;
import io.artoor.query.One;
import io.artoor.query.Queryable;
import io.artoor.value.Int32;
import io.artoor.value.Numeral;
import io.artoor.value.Single64;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static io.artoor.type.Nullability.nonNullable;
import static io.artoor.type.Numeric.asNumber;
import static io.artoor.value.Numeral.asNumeral;
import static io.artoor.value.Numeral.asSingle64;
import static java.util.function.Function.identity;

public interface Averageable<T extends Record> extends Queryable<T> {
  default <V extends Record, N extends Number, R extends Record & Numeral<N, R>> One<R> average(final BiFunction<? super Integer, ? super T, ? extends V> select, final Function<? super V, ? extends R> asNumber) {
    final var s = nonNullable(select, "select");
    final var n = nonNullable(asNumber, "asNumber");
    return new Average<T, V, N, R>(this, (i, it) -> {}, (i, it) -> true, s, n)::iterator;
  }

  default <V extends Record, N extends Number, R extends Record & Numeral<N, R>> One<R> average(final Function<? super T, ? extends V> select, final Function<? super V, ? extends R> asNumeral) {
    final var s = nonNullable(select, "select");
    final var n = nonNullable(asNumeral, "asNumeral");
    return this.<V, N, R>average((index, it) -> s.apply(it), n);
  }

  default <N extends Number, R extends Record & Numeral<N, R>> One<R> average(final BiFunction<? super Integer, ? super T, ? extends R> select) {
    return this.<R, N, R>average(select, asNumeral());
  }

  default <N extends Number, R extends Record & Numeral<N, R>> One<R> average(final Function<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return this.average((index, it) -> select.apply(it));
  }

  default One<Single64> average() {
    return this.<T, Double, Single64>average(identity(), asSingle64());
  }
}

final class Average<T extends Record, V extends Record, N extends Number, R extends Record & Numeral<N, R>> implements Averageable<R> {
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
    return count == 0 ? Cursor.none() : Cursor.of(total.div(new Int32(count)));
  }
}



