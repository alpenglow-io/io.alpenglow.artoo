package io.artoo.lance.query.many;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

import static io.artoo.lance.type.Nullability.nonNullable;

interface Summable<T> extends Queryable<T> {
  default <N extends Number> One<N> sum(final Func.Uni<? super T, ? extends N> select) {
    final var sel = nonNullable(select, "select");
    return new Sum<>(this, it -> {}, sel);
  }

  default One<T> sum() {
    return new Sum<>(this, it -> {}, it -> it instanceof Number n ? n : null);
  }
}

@SuppressWarnings({"unchecked"})
final class Sum<T, N extends Number, V> implements One<V> {
  private final Queryable<T> queryable;
  private final Cons.Uni<? super T> peek;
  private final Func.Uni<? super T, ? extends N> select;

  Sum(final Queryable<T> queryable, final Cons.Uni<? super T> peek, final Func.Uni<? super T, ? extends N> select) {
    assert queryable != null && peek != null && select != null;
    this.queryable = queryable;
    this.peek = peek;
    this.select = select;
  }

  @NotNull
  @Override
  public Iterator<V> iterator() {
    N result = null;
    for (final var record : queryable) {
      if (record != null) {
        peek.accept(record);
        final var selected = select.apply(record);
        if (selected != null) {
          if (selected instanceof Byte val) result = (N) Byte.valueOf((byte) (result == null ? val : result.byteValue() + val));
          if (selected instanceof Short val) result = (N) Short.valueOf((short) (result == null ? val : result.shortValue() + val));
          if (selected instanceof Integer val) result = (N) Integer.valueOf(result == null ? val : result.intValue() + val);
          if (selected instanceof Long val) result = (N) Long.valueOf(result == null ? val : result.longValue() + val);

          if (selected instanceof Float val) result = (N) Float.valueOf(result == null ? val : result.floatValue() + val);
          if (selected instanceof Double val) result = (N) Double.valueOf(result == null ? val : result.doubleValue() + val);

          if (selected instanceof BigInteger val) result = (N) BigInteger.valueOf(result == null ? val.longValue() : result.longValue() + val.longValue());
          if (selected instanceof BigDecimal val) result = (N) BigDecimal.valueOf(result == null ? val.doubleValue() : result.doubleValue() + val.doubleValue());
        }
      }
    }

    return Cursor.of((V) result);
  }
}

