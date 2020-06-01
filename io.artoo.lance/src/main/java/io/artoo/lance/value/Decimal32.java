package io.artoo.lance.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Decimal32(float eval) implements Numeral<Decimal32> {
  public static final Decimal32 ZERO = new Decimal32(0.0f);
  public static final Decimal32 ONE = new Decimal32(1.0f);

  @Contract("_ -> new")
  public static @NotNull Decimal32 let(final float value) {
    return new Decimal32(value);
  }

  @Override
  public final @NotNull Float raw() {
    return eval;
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeral<V>> @NotNull Decimal32 add(@NotNull V value) {
    return new Decimal32(eval + value.raw().floatValue());
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeral<V>> @NotNull Decimal32 div(@NotNull V value) {
    return new Decimal32(eval / value.raw().floatValue());
  }

  @Override
  @Contract(" -> new")
  public final @NotNull Decimal32 inc() {
    return new Decimal32(eval + 1);
  }
}

