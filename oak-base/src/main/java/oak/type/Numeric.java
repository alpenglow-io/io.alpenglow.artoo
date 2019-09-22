package oak.type;

import oak.func.fun.Function1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.isNull;

public enum Numeric {
  NaN,
  Double,
  Integer,
  Long,
  BigInteger,
  BigDecimal;

  public static <N> Numeric fromAny(@NotNull final N maybeNumber) {
    try {
      return Numeric.valueOf(maybeNumber.getClass().getSimpleName());
    } catch (Exception e) {
      return Numeric.NaN;
    }
  }

  public static <N extends Number> Numeric from(@NotNull final N number) {
    try {
      return Numeric.valueOf(number.getClass().getSimpleName());
    } catch (Exception e) {
      return Numeric.NaN;
    }
  }

  @Contract(pure = true)
  public static <T> Number asNumber(final T value) { return (Number) value; }

  @NotNull
  @Contract(pure = true)
  public static <V> Function1<? super V, Double> asDouble() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Double, BigDecimal, Integer, Long, BigInteger -> asNumber(it).doubleValue();
      case NaN -> null;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Function1<? super V, Float> asFloat() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Double, BigDecimal, Integer, Long, BigInteger -> asNumber(it).floatValue();
      case NaN -> null;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Function1<? super V, Integer> asInteger() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Double, BigDecimal, Integer, Long, BigInteger -> asNumber(it).intValue();
      case NaN -> null;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Function1<? super V, Long> asLong() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Double, BigDecimal, Integer, Long, BigInteger -> asNumber(it).longValue();
      case NaN -> null;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Function1<? super V, Short> asShort() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Double, BigDecimal, Integer, Long, BigInteger -> asNumber(it).shortValue();
      case NaN -> null;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Function1<? super V, Byte> asByte() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Double, BigDecimal, Integer, Long, BigInteger -> asNumber(it).byteValue();
      case NaN -> null;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Function1<? super V, java.math.BigInteger> asBigInteger() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Double, BigDecimal, Integer, Long, BigInteger -> java.math.BigInteger.valueOf(asNumber(it).longValue());
      case NaN -> null;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Function1<? super V, java.math.BigDecimal> asBigDecimal() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Double, BigDecimal, Integer, Long, BigInteger -> java.math.BigDecimal.valueOf(asNumber(it).doubleValue());
      case NaN -> null;
    };
  }
}
