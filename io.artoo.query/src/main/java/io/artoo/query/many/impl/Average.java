package io.artoo.query.many.impl;

import io.artoo.cursor.Cursor;




import io.artoo.query.Queryable;
import io.artoo.query.many.Averageable;
import io.artoo.type.Numeric;
import io.artoo.value.Int32;
import io.artoo.value.Numeral;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static io.artoo.type.Numeric.divide;

public final class Average<T extends Record, V extends Record, N extends Number, R extends Record & Numeral<N, R>> implements Averageable<R> {
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
