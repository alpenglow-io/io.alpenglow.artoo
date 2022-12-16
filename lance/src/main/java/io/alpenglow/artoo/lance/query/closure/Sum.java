package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Closure;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings({"unchecked"})
public final class Sum<T, N extends Number, V> implements Closure<T, V> {
  private final TryFunction1<? super T, ? extends N> function;
  private V summed;

  public Sum(final TryFunction1<? super T, ? extends N> function) {
    this.function = function;
    this.summed = null;
  }

  @Override
  public V invoke(final T element) throws Throwable {
    return (summed = sum(function.invoke(element), (N) summed));
  }

  private V sum(final N selected, final N number) {
    return switch (selected) {
      case null           -> (V) number;
      case Byte val       -> (V) Byte.valueOf((byte) (number.byteValue() + val));
      case Short val      -> (V) Short.valueOf((short) (number.shortValue() + val));
      case Integer val    -> (V) Integer.valueOf(number.intValue() + val);
      case Long val       -> (V) Long.valueOf(number.longValue() + val);
      case Float val      -> (V) Float.valueOf(number.floatValue() + val);
      case Double val     -> (V) Double.valueOf(number.doubleValue() + val);
      case BigInteger val -> (V) BigInteger.valueOf(number.longValue() + val.longValue());
      case BigDecimal val -> (V) BigDecimal.valueOf(number.doubleValue() + val.doubleValue());
      default             -> number == null ? (V) selected : illegalState(selected);
    };
  }
  private V illegalState(N selected) {
    throw new IllegalStateException("Can't cast to unknown number type: " + selected.getClass().getName());
  }
}
