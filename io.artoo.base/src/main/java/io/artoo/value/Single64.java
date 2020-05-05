package io.artoo.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public record Single64(double eval) implements Numeral<Double, Single64> {
  public static final Single64 ZERO = new Single64(0.0d);
  public static final Single64 ONE = new Single64(1.0d);

  public final @NotNull BigDecimal asBigDecimal() {
    return BigDecimal.valueOf(eval);
  }

  @Override
  public final Double box() {
    return eval;
  }

  @Override
  @Contract(pure = true)
  public final @NotNull Single64 add(@NotNull Single64 value) {
    return new Single64(eval + value.eval);
  }

  @Override
  @Contract(" -> new")
  public final @NotNull Single64 inc() {
    return new Single64(eval + 1);
  }
}

