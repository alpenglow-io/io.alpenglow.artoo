package re.artoo.lance.query.closure;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Closure;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("unchecked")
public final class Extreme<T, N> implements Closure<T, N> {
  private static final int Greater = 1;
  private static final int Lesser = -1;
  private final int type;
  private final TryFunction1<? super T, ? extends N> select;
  private N extreme;

  private Extreme(final N extreme, final int type, final TryFunction1<? super T, ? extends N> select) {
    this.extreme = extreme;
    this.type = type;
    this.select = select;
  }

  public static <T> Extreme<T, T> max() {
    return new Extreme<>(null, Extreme.Greater, it -> it);
  }

  public static <T, N extends Number> Extreme<T, N> max(TryFunction1<? super T, ? extends N> selector) {
    return new Extreme<>(null, Extreme.Greater, selector);
  }

  public static <T> Extreme<T, T> min() {
    return new Extreme<>(null, Extreme.Lesser, it -> it);
  }

  public static <ELEMENT, NUMBER extends Number> Extreme<ELEMENT, NUMBER> min(TryFunction1<? super ELEMENT, ? extends NUMBER> selector) {
    return new Extreme<>(null, Extreme.Lesser, selector);
  }

  @SuppressWarnings("unchecked")
  @Override
  public N invoke(final T element) throws Throwable {
    return switch (select.invoke(element)) {
      case Number number when (extreme instanceof Number current && compareBy(current, number) == type) || extreme == null -> (extreme = (N) number);
      case null, default -> extreme;
    };
  }

  private int compareBy(Number current, Number selected) {
    return switch (selected) {
      case Byte it -> it > current.byteValue() ? Greater : Lesser;
      case Short it -> it > current.shortValue() ? Greater : Lesser;
      case Integer it -> it > current.intValue() ? Greater : Lesser;
      case Long it -> it > current.longValue() ? Greater : Lesser;
      case Float it -> it > current.floatValue() ? Greater : Lesser;
      case Double it -> it > current.doubleValue() ? Greater : Lesser;
      case BigInteger it -> it.longValue() > BigInteger.valueOf(current.longValue()).longValue() ? Greater : Lesser;
      case BigDecimal it -> it.doubleValue() > BigDecimal.valueOf(current.doubleValue()).doubleValue() ? Greater : Lesser;
      case null -> type * -1;
      default -> type;
    };
  }
}
