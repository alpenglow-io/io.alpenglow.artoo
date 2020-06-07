package io.artoo.lance.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public record Single64(double eval) implements Numeric<Single64> {
  public static final Single64 ZERO = new Single64(0.0d);
  public static final Single64 ONE = new Single64(1.0d);

  @Contract("_ -> new")
  public static @NotNull Single64 let(final double value) {
    return new Single64(value);
  }

  public static Single64 let(final Number number) {
    return new Single64(number.doubleValue());
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
  public <V extends Record & Numeric<V>> @NotNull Single64 add(@NotNull V value) {
    return new Single64(eval + value.raw().doubleValue());
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeric<V>> @NotNull Single64 div(@NotNull V value) {
    return new Single64(eval / value.raw().doubleValue());
  }

  @Override
  @Contract(" -> new")
  public final @NotNull Single64 inc() {
    return new Single64(eval + 1);
  }
}

