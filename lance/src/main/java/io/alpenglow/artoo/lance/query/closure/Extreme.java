package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Closure;

import java.math.BigDecimal;
import java.math.BigInteger;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public final class Extreme<T, R extends Number, V> implements Closure<T, V> {
  private static final int Maximum = 1;
  private static final int Minimum = -1;

  private V extreme;
  private final int type;
  private final TryFunction1<? super T, ? extends R> select;

  private Extreme(final V extreme, final int type, final TryFunction1<? super T, ? extends R> select) {
    this.extreme = extreme;
    this.type = type;
    this.select = select;
  }

  @SuppressWarnings("unchecked")
  @Override
  public V invoke(final T element) throws Throwable {
    if (element != null) {
      final var selected = select.invoke(element);
      if (compare((R) extreme, selected) == type) {
        return (extreme = (V) selected);
      }
    }
    return extreme;
  }

  private int compare(final R compared, final R selected) {
    if (compared == null && selected != null)
      return type;
    return switch (requireNonNull(selected)) {
      case Byte b -> b > requireNonNull(compared).byteValue() ? 1 : -1;
      case Short s -> s > requireNonNull(compared).shortValue() ? 1 : -1;
      case Integer i -> i > requireNonNull(compared).intValue() ? 1 : -1;
      case Long l -> l > requireNonNull(compared).longValue() ? 1 : -1;
      case Float f -> f > requireNonNull(compared).floatValue() ? 1 : -1;
      case Double d -> d > requireNonNull(compared).doubleValue() ? 1 : -1;
      case BigInteger it -> it.longValue() > BigInteger.valueOf(requireNonNull(compared).longValue()).longValue() ? 1 : -1;
      case BigDecimal it -> it.doubleValue() > BigDecimal.valueOf(requireNonNull(compared).doubleValue()).doubleValue() ? 1 : -1;

      default -> type * -1;
    };
  }

  public static <T, R extends Number, V> Extreme<T, R, V> max() {
    return new Extreme<>(null, Extreme.Maximum, it -> (R) it);
  }

  public static <T, R extends Number, V> Extreme<T, R, V> max(TryFunction1<? super T, ? extends R> selector) {
    return new Extreme<>(null, Extreme.Maximum, selector);
  }

  public static <T, R extends Number, V> Extreme<T, R, V> min() {
    return new Extreme<>(null, Extreme.Minimum, it -> (R) it);
  }

  public static <T, R extends Number, V> Extreme<T, R, V> min(TryFunction1<? super T, ? extends R> selector) {
    return new Extreme<>(null, Extreme.Minimum, selector);
  }
}
