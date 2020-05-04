package io.artoo.query.many.impl;

import io.artoo.cursor.Cursor;




import io.artoo.query.Queryable;
import io.artoo.type.Numeric;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static io.artoo.type.Numeric.divide;

public final class Average<T, V, N extends Number> implements Queryable<N> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final BiPredicate<? super Integer, ? super T> where;
  private final BiFunction<? super Integer, ? super T, ? extends V> select;
  private final Function<? super V, ? extends N> asNumber;

  @Contract(pure = true)
  public Average(
    final Queryable<T> queryable,
    final BiConsumer<? super Integer, ? super T> peek,
    final BiPredicate<? super Integer, ? super T> where,
    final BiFunction<? super Integer, ? super T, ? extends V> select,
    final Function<? super V, ? extends N> asNumber
  ) {
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
    this.select = select;
    this.asNumber = asNumber;
  }

  @NotNull
  @Override
  public final Iterator<N> iterator() {
    N total = null;
    N count = null;
    var index = 0;
    for (var iterator = queryable.iterator(); iterator.hasNext(); index++) {
      var next = iterator.next();
      peek.accept(index, next);
      if (next != null && where.test(index, next)) {
        final var value = select.andThen(asNumber).apply(index, next);
        if (value != null) {
          total = Numeric.sum(total, value);
          count = Numeric.sum(count, Numeric.one(value));
        }
      }
    }
    return count == null || count.equals(zero(count)) ? Cursor.none() : Cursor.of(divide(total, count));
  }
}
