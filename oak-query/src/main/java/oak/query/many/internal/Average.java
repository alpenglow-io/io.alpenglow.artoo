package oak.query.many.internal;

import oak.cursor.Cursor;
import oak.func.$2.IntCons;
import oak.func.$2.IntFunc;
import oak.func.$2.IntPred;
import oak.func.Func;
import oak.query.One;
import oak.query.Queryable;
import oak.type.Numeric;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.type.Numeric.divide;
import static oak.type.Numeric.zero;

public final class Average<T, V, N extends Number> implements Queryable<N> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final IntPred<? super T> where;
  private final IntFunc<? super T, ? extends V> select;
  private final Func<? super V, ? extends N> asNumber;

  @Contract(pure = true)
  public Average(
    final Queryable<T> queryable,
    final IntCons<? super T> peek,
    final IntPred<? super T> where,
    final IntFunc<? super T, ? extends V> select,
    final Func<? super V, ? extends N> asNumber
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
      peek.acceptInt(index, next);
      if (next != null && where.verify(index, next)) {
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
