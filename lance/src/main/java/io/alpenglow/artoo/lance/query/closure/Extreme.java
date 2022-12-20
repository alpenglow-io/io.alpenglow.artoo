package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Closure;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("unchecked")
public final class Extreme<T, R extends Number, V> implements Closure<T, V> {
  private static final int Maximum = 1;
  private static final int Minimum = -1;
  private final int type;
  private final TryFunction1<? super T, ? extends R> select;
  private V extreme;

  private Extreme(final V extreme, final int type, final TryFunction1<? super T, ? extends R> select) {
    this.extreme = extreme;
    this.type = type;
    this.select = select;
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

  @SuppressWarnings("unchecked")
  @Override
  public V invoke(final T element) throws Throwable {
    final var selected = select.invoke(element);
    if (compareToCurrent(selected) == type) {
      return (extreme = (V) selected);
    }
    return extreme;
  }

  private int compareToCurrent(final R selected) {
    final var current = (R) extreme;
    return switch (selected) {
      case Byte it when current != null -> it > current.byteValue() ? 1 : -1;
      case Short it when current != null -> it > current.shortValue() ? 1 : -1;
      case Integer it when current != null -> it > current.intValue() ? 1 : -1;
      case Long it when current != null -> it > current.longValue() ? 1 : -1;
      case Float it when current != null -> it > current.floatValue() ? 1 : -1;
      case Double it when current != null -> it > current.doubleValue() ? 1 : -1;
      case BigInteger it when current != null -> it.longValue() > BigInteger.valueOf(current.longValue()).longValue() ? 1 : -1;
      case BigDecimal it when current != null -> it.doubleValue() > BigDecimal.valueOf(current.doubleValue()).doubleValue() ? 1 : -1;
      case null -> type * -1;
      default -> type;
    };
  }
}
