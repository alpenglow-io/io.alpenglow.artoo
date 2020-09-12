package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.next.Cursor;

import java.math.BigDecimal;
import java.math.BigInteger;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Extremable<T> extends Projectable<T> {
  default Cursor<T> extreme(int type) {
    return extreme(type, it -> it instanceof Number n ? n : null);
  }

  default <N extends Number, V> Cursor<V> extreme(int type, final Func.Uni<? super T, ? extends N> select) {
    return select(new Extremum<T, N, V>(type, select));
  }
}

final class Extremum<T, N extends Number, V> implements Func.Uni<T, V> {
  private final class Extremed { private V value; }

  private final Extremed extremed = new Extremed();
  private final int extreme;
  private final Func.Uni<? super T, ? extends N> number;

  Extremum(final int extreme, final Func.Uni<? super T, ? extends N> number) {
    assert number != null;
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
