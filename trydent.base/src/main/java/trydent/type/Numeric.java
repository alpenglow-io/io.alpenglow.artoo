package trydent.type;

import trydent.func.Func;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;

import static java.math.RoundingMode.CEILING;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNullElse;
import static java.util.Objects.requireNonNullElseGet;

@SuppressWarnings("unchecked")
public enum Numeric {
  NaN,
  Integer,
  Long,
  Float,
  Double,
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

  public static <N extends Number> N sum(@Nullable final N result, @NotNull final N value) {
    var sum = isNull(result) ? zero(value) : result;
    return (N) switch (Numeric.from(value)) {
      case Integer -> sum.intValue() + value.intValue();
      case Long -> sum.longValue() + value.longValue();
      case Float -> sum.floatValue() + value.floatValue();
      case Double -> sum.doubleValue() + value.doubleValue();
      case BigInteger -> java.math.BigInteger.valueOf(sum.longValue()).add(java.math.BigInteger.valueOf(value.longValue()));
      case BigDecimal -> java.math.BigDecimal.valueOf(sum.doubleValue()).add(java.math.BigDecimal.valueOf(value.doubleValue()));
      case NaN -> throw new IllegalStateException("Unexpected value: " + sum);
    };
  }

  public static <N extends Number> N divide(@Nullable final N dividend, @Nullable final N divisor) {
    return divide(dividend, divisor, CEILING);
  }

  @Nullable
  public static <N extends Number> N divide(@Nullable final N dividend, @Nullable final N divisor, RoundingMode rounding) {
    if (isNull(dividend) || isNull(divisor) || divisor.intValue() == 0) return null;
    return (N) switch (from(dividend)) {
      case NaN -> throw new IllegalStateException("Unexpected value: " + from(dividend));
      case Integer -> dividend.intValue() / divisor.intValue();
      case Long -> dividend.longValue() / divisor.longValue();
      case Float -> dividend.floatValue() / divisor.floatValue();
      case Double -> dividend.doubleValue() / divisor.doubleValue();
      case BigInteger -> java.math.BigInteger.valueOf(dividend.longValue()).divide(java.math.BigInteger.valueOf(divisor.longValue()));
      case BigDecimal -> java.math.BigDecimal.valueOf(dividend.doubleValue()).divide(java.math.BigDecimal.valueOf(divisor.doubleValue()), rounding);
    };
  }

  @Contract(pure = true)
  public static <T> Number asNumber(final T value) {
    return (Number) value;
  }

  public static <T> Number asNumber(final T value, final String message, final Func<? super String, ? extends RuntimeException> throwing) {
    final var error = requireNonNullElse(message, "Can't cast to number.");
    final Func<? super String, ? extends RuntimeException> thrown = requireNonNullElseGet(throwing, () -> ((Func<? super String, ? extends RuntimeException>) ClassCastException::new));
    try {
      return asNumber(value);
    } catch (ClassCastException e) {
      throw thrown.apply(error);
    }
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Func<? super V, Double> asDouble() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Float, Double, BigDecimal, Integer, Long, BigInteger -> asNumber(it).doubleValue();
      case NaN -> null;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V, N extends Number> Func<? super V, ? extends N> asNumber() {
    return it -> it == null ? null : switch (Numeric.fromAny(it)) {
      case NaN -> null;
      case Integer, BigInteger, Long, Float, Double, BigDecimal -> (N) it;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Func<? super V, Float> asFloat() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Float, Double, BigDecimal, Integer, Long, BigInteger -> asNumber(it).floatValue();
      case NaN -> null;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Func<? super V, Integer> asInteger() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Float, Double, BigDecimal, Integer, Long, BigInteger -> asNumber(it).intValue();
      case NaN -> null;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Func<? super V, Long> asLong() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Float, Double, BigDecimal, Integer, Long, BigInteger -> asNumber(it).longValue();
      case NaN -> null;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Func<? super V, Short> asShort() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Float, Double, BigDecimal, Integer, Long, BigInteger -> asNumber(it).shortValue();
      case NaN -> null;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Func<? super V, Byte> asByte() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Float, Double, BigDecimal, Integer, Long, BigInteger -> asNumber(it).byteValue();
      case NaN -> null;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Func<? super V, java.math.BigInteger> asBigInteger() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Float, Double, BigDecimal, Integer, Long, BigInteger -> java.math.BigInteger.valueOf(asNumber(it).longValue());
      case NaN -> null;
    };
  }

  @NotNull
  @Contract(pure = true)
  public static <V> Func<? super V, java.math.BigDecimal> asBigDecimal() {
    return it -> isNull(it) ? null : switch (Numeric.fromAny(it)) {
      case Float, Double, BigDecimal, Integer, Long, BigInteger -> java.math.BigDecimal.valueOf(asNumber(it).doubleValue());
      case NaN -> null;
    };
  }

  public static <N extends Number> N zero(final N number) {
    return (N) switch (Numeric.from(number)) {
      case NaN -> throw new IllegalStateException("Unexpected value: " + Numeric.from(number));
      case Integer -> 0;
      case Long -> 0L;
      case Float -> 0.0;
      case Double -> 0.0D;
      case BigInteger -> java.math.BigInteger.ZERO;
      case BigDecimal -> java.math.BigDecimal.ZERO;
    };
  }

  public static <N extends Number> N one(final N number) {
    return (N) switch (Numeric.from(number)) {
      case NaN -> throw new IllegalStateException("Unexpected value: " + Numeric.from(number));
      case Integer -> 1;
      case Long -> 1L;
      case Float -> 1.0;
      case Double -> 1.0D;
      case BigInteger -> java.math.BigInteger.ONE;
      case BigDecimal -> java.math.BigDecimal.ONE;
    };
  }
}
