package io.alpenglow.artoo.lance.query.func;

import io.alpenglow.artoo.lance.func.TryFunction1;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings({"unchecked"})
public final class Sum<T, N extends Number, V> implements TryFunction1<T, V> {
  private final TryFunction1<? super T, ? extends N> select;
  private final Summed summed;

  public Sum(final TryFunction1<? super T, ? extends N> select) {
    assert select != null;
    this.select = select;
    this.summed = new Summed();
  }

  @Override
  public V tryApply(final T element) throws Throwable {
    return (summed.value = sum(select.tryApply(element), (N) summed.value));
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

  private final class Summed {
    private V value;
  }
}
