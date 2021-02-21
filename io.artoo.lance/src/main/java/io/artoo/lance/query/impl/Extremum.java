package io.artoo.lance.query.impl;

import io.artoo.lance.func.Func;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("unchecked")
public final class Extremum<T, N extends Number, V> implements Func.Uni<T, V> {
  private final class Extremed { private V value; }

  private final Extremed extremed;
  private final int extreme;
  private final Func.Uni<? super T, ? extends N> number;

  public Extremum(final int extreme, final Func.Uni<? super T, ? extends N> number) {
    assert number != null;
    this.extremed = new Extremed();
    this.extreme = extreme;
    this.number = number;
  }

  @Override
  public V tryApply(final T element) throws Throwable {
    if (element == null) return extremed.value;

    final var numbered = number.tryApply(element);
    if (compare((N) extremed.value, numbered) == extreme) {
      extremed.value = (V) numbered;
    }
    return extremed.value;
  }

  private int compare(final N compared, final N numbered) {
    if (compared == null && numbered != null) {
      return extreme;

    } else if (numbered instanceof Byte b) {
      return b > compared.byteValue() ? 1 : -1;

    } else if (numbered instanceof Short s) {
      return s > compared.shortValue() ? 1 : -1;

    } else if (numbered instanceof Integer i) {
      return i > compared.intValue() ? 1 : -1;

    } else if (numbered instanceof Long l) {
      return l > compared.longValue() ? 1 : -1;

    } else if (numbered instanceof Float f) {
      return f > compared.floatValue() ? 1 : -1;

    } else if (numbered instanceof Double d) {
      return d > compared.doubleValue() ? 1 : -1;

    } else if (numbered instanceof BigInteger it) {
      return it.longValue() > BigInteger.valueOf(compared.longValue()).longValue() ? 1 : -1;

    } else if (numbered instanceof BigDecimal it) {
      return it.doubleValue() > BigDecimal.valueOf(compared.doubleValue()).doubleValue() ? 1 : -1;
    }

    return extreme * -1;
  }
}
