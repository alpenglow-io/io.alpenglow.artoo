package io.artoo.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Single32(float eval) implements Numeral<Single32> {
  public static final Single32 ZERO = new Single32(0.0f);
  public static final Single32 ONE = new Single32(1.0f);

  @Contract("_ -> new")
  public static @NotNull Single32 let(final float value) {
    return new Single32(value);
  }

  @Override
  public final @NotNull Float raw() {
    return eval;
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeral<V>> @NotNull Single32 add(@NotNull V value) {
    return new Single32(eval + value.raw().floatValue());
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeral<V>> @NotNull Single32 div(@NotNull V value) {
    return new Single32(eval / value.raw().floatValue());
  }

  @Override
  @Contract(" -> new")
  public final @NotNull Single32 inc() {
    return new Single32(eval + 1);
  }
}

