package io.artoo.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Single32(float eval) implements Numeral<Float, Single32> {
  public static final Single32 ZERO = new Single32(0.0f);
  public static final Single32 ONE = new Single32(1.0f);

  @Override
  public final @NotNull Float box() {
    return eval;
  }

  @Override
  @Contract("_ -> new")
  public final @NotNull Single32 add(@NotNull Single32 value) {
    return new Single32(eval + value.eval);
  }

  @Override
  @Contract(" -> new")
  public final @NotNull Single32 inc() {
    return new Single32(eval + 1);
  }
}

