package io.artoo.value;

import io.artoo.type.Numeric;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@SuppressWarnings("unchecked")
public interface Numeral<N extends Number, R extends Record & Numeral<N, R>> {
  N box();

  R add(R value);

  static <N extends Number, R extends Record & Numeral<N, R>> R add(R first, R second) {
    return first == null ? second : first.add(second);
  }

  @NotNull
  @Contract(pure = true)
  static <V, N extends Number, R extends Record & Numeral<N, R>> Function<? super V, ? extends R> asNumber() {
    return it -> it == null ? null : (R) switch (Numeric.fromAny(it)) {
      case Long -> new Int64((Long) it);
      case Float -> new Single32((Float) it);
      case Double -> new Single64((Double) it);
      case NaN, BigDecimal, BigInteger -> null;
      case Integer -> new Int32((Integer) it);
    };
  }
}
