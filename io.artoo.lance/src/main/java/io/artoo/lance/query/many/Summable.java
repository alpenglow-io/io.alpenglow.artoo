package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import static io.artoo.lance.type.Nullability.nonNullable;

interface Summable<T> extends Queryable<T> {
  default <N extends Number> One<N> sum(final Func.Uni<? super T, ? extends N> select) {
    final var sel = nonNullable(select, "select");
    return new Sum<>(this, sel);
  }

  default One<T> sum() {
    return new Sum<>(this, it -> it instanceof Number n ? n : null);
  }
}

@SuppressWarnings({"unchecked"})
final class Sum<T, N extends Number, V> implements One<V> {
  private final Queryable<T> queryable;
  private final Func.Uni<? super T, ? extends N> select;

  Sum(final Queryable<T> queryable, final Func.Uni<? super T, ? extends N> select) {
    assert queryable != null && select != null;
    this.queryable = queryable;
    this.select = select;
  }

  @NotNull
  @Override
  public Cursor<V> cursor() {
    final var summed = Cursor.<V>local();

    final var cursor = queryable.cursor();
    try {
      while (cursor.hasNext()) {
        summed.set(
          sum(
            cursor.fetch(select::tryApply),
            (N) summed.next()
          )
        );
      }
    } catch (Throwable throwable) {
      summed.grab(throwable);
    }

    return summed;
  }

  private V sum(final N selected, final N number) {
    if (selected == null) {
      return (V) number;

    } else if (number == null) {
      return (V) selected;

    } else if (selected instanceof Byte val) {
      return (V) Byte.valueOf((byte) (number.byteValue() + val));

    } else if (selected instanceof Short val) {
      return (V) Short.valueOf((short) (number.shortValue() + val));

    } else if (selected instanceof Integer val) {
      return (V) Integer.valueOf(number.intValue() + val);

    } else if (selected instanceof Long val) {
      return (V) Long.valueOf(number.longValue() + val);

    } else if (selected instanceof Float val) {
      return (V) Float.valueOf(number.floatValue() + val);

    } else if (selected instanceof Double val) {
      return (V) Double.valueOf(number.doubleValue() + val);

    } else if (selected instanceof BigInteger val) {
      return (V) BigInteger.valueOf(number.longValue() + val.longValue());

    } else if (selected instanceof BigDecimal val) {
      return (V) BigDecimal.valueOf(number.doubleValue() + val.doubleValue());

    }

    throw new IllegalStateException("Can't cast to unknown number type: " + selected.getClass().getName());
  }
}

