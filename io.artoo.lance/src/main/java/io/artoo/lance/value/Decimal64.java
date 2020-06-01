package io.artoo.lance.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public record Decimal64(double eval) implements Numeral<Decimal64> {
  public static final Decimal64 ZERO = new Decimal64(0.0d);
  public static final Decimal64 ONE = new Decimal64(1.0d);

  @Contract("_ -> new")
  public static @NotNull Decimal64 let(final double value) {
    return new Decimal64(value);
  }

  public static Decimal64 let(final Number number) {
    return new Decimal64(number.doubleValue());
  }

  public final @NotNull BigDecimal asBigDecimal() {
    return BigDecimal.valueOf(eval);
  }

  @Override
  public final Number raw() {
    return eval;
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeral<V>> @NotNull Decimal64 add(@NotNull V value) {
    return new Decimal64(eval + value.raw().doubleValue());
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeral<V>> @NotNull Decimal64 div(@NotNull V value) {
    return new Decimal64(eval / value.raw().doubleValue());
  }

  @Override
  @Contract(" -> new")
  public final @NotNull Decimal64 inc() {
    return new Decimal64(eval + 1);
  }
}

