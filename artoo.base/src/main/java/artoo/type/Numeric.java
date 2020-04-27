package artoo.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import artoo.func.Func;

import java.math.BigInteger;
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
    var sum = result == null ? zero(value) : result;

    if (value instanceof Integer v) {
      return (N) java.lang.Integer.valueOf(sum.intValue() + v);

    } else if (value instanceof Long v) {
      return (N) java.lang.Long.valueOf(sum.longValue() + v);

    } else if (value instanceof Float v) {
      return (N) java.lang.Float.valueOf(sum.floatValue() + v);

    } else if (value instanceof Double v) {
      return (N) java.lang.Double.valueOf(sum.doubleValue() + v);

    } else if (value instanceof BigInteger v) {
      return (N) java.math.BigInteger.valueOf(sum.longValue()).add(v);

    } else if (value instanceof java.math.BigDecimal v) {
      return (N) java.math.BigDecimal.valueOf(sum.doubleValue()).add(v);

    } else {
      throw new IllegalStateException("Unexpected value: " + sum);
    }
  }

  public static <N extends Number> N divide(@Nullable final N dividend, @Nullable final N divisor) {
    return divide(dividend, divisor, CEILING);
  }

  @Nullable
  public static <N extends Number> N divide(@Nullable final N dividend, @Nullable final N divisor, RoundingMode rounding) {
    if (isNull(dividend) || isNull(divisor) || divisor.intValue() == 0)
      return null;

    if (dividend instanceof Integer r && divisor instanceof Integer v) {
      return (N) java.lang.Integer.valueOf(r / v);

    } else if (dividend instanceof Long r && divisor instanceof Long v) {
      return (N) java.lang.Long.valueOf(r / v);

    } else if (dividend instanceof Float r && divisor instanceof Float v) {
      return (N) java.lang.Float.valueOf(r / v);

    } else if (dividend instanceof Double r && divisor instanceof Double v) {
      return (N) java.lang.Double.valueOf(r / v);

    } else if (dividend instanceof java.math.BigInteger r && divisor instanceof BigInteger v) {
      return (N) r.divide(v);

    } else if (dividend instanceof java.math.BigDecimal r && divisor instanceof java.math.BigDecimal v) {
      return (N) r.divide(v, rounding);

    } else {
      throw new IllegalStateException("Unexpected value: " + dividend);
    }
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
      case Float -> 0.0F;
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
