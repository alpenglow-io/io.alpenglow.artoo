package re.artoo.lance.query.closure;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Closure;

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
      case null, default -> (V) sum;
      case Byte val       -> sum != null ? (V) Byte.valueOf((byte) (sum.byteValue() + val)) : (V) val;
      case Short val      -> sum != null ? (V) Short.valueOf((short) (sum.shortValue() + val)) : (V) val;
      case Integer val    -> sum != null ? (V) Integer.valueOf(sum.intValue() + val) : (V) val;
      case Long val       -> sum != null ? (V) Long.valueOf(sum.longValue() + val) : (V) val;
      case Float val      -> sum != null ? (V) Float.valueOf(sum.floatValue() + val) : (V) val;
      case Double val     -> sum != null ? (V) Double.valueOf(sum.doubleValue() + val) : (V) val;
      case BigInteger val -> sum != null ? (V) BigInteger.valueOf(sum.longValue() + val.longValue()) : (V) val;
      case BigDecimal val -> sum != null ? (V) BigDecimal.valueOf(sum.doubleValue() + val.doubleValue()) : (V) val;
    };
  }

}
