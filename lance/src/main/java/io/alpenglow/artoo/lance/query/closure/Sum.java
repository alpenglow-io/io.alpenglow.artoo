package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Closure;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings({"unchecked"})
public final class Sum<T, N, V> implements Closure<T, V> {
  private final TryFunction1<? super T, ? extends N> select;
  private V summed;

  public Sum(final TryFunction1<? super T, ? extends N> select) {
    this.select = select;
    this.summed = null;
  }

  @Override
  public V invoke(final T element) throws Throwable {
    return (summed = sum(select.invoke(element), summed instanceof Number it ? it : null));
  }

  private V sum(final N selected, final Number sum) {
    return switch (selected) {
      case null -> (V) sum;
      case Byte val       when sum != null -> (V) Byte.valueOf((byte) (sum.byteValue() + val));
      case Short val      when sum != null -> (V) Short.valueOf((short) (sum.shortValue() + val));
      case Integer val    when sum != null -> (V) Integer.valueOf(sum.intValue() + val);
      case Long val       when sum != null -> (V) Long.valueOf(sum.longValue() + val);
      case Float val      when sum != null -> (V) Float.valueOf(sum.floatValue() + val);
      case Double val     when sum != null -> (V) Double.valueOf(sum.doubleValue() + val);
      case BigInteger val when sum != null -> (V) BigInteger.valueOf(sum.longValue() + val.longValue());
      case BigDecimal val when sum != null -> (V) BigDecimal.valueOf(sum.doubleValue() + val.doubleValue());
      default -> sum == null ? (V) selected : illegalState(selected);
    };
  }

  private V illegalState(N selected) {
    throw new IllegalStateException("Can't cast to unknown number type: " + selected.getClass().getName());
  }
}
