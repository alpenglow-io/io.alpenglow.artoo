package io.artoo.lance.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record UInt32(int eval) implements Numeral<UInt32> {
  public UInt32 { assert eval >= 0; }

  public static final UInt32 ZERO = new UInt32(0);
  public static final UInt32 ONE = new UInt32(1);

  @Override
  public final @NotNull Integer raw() {
    return eval;
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeral<V>> @NotNull UInt32 add(@NotNull V value) {
    return new UInt32(eval + value.raw().intValue());
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeral<V>> @NotNull UInt32 div(@NotNull V value) {
    return new UInt32(eval / value.raw().intValue());
  }

  @Override
  @Contract(" -> new")
  public final @NotNull UInt32 inc() {
    return new UInt32(eval + 1);
  }
}

