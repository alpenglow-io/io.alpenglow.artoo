package io.artoo.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record UInt32(int eval) implements Numeral<Integer, UInt32> {
  public UInt32 { if (eval < 0) throw new IllegalStateException("UInt32 can't be negative."); }

  public static final UInt32 ZERO = new UInt32(0);
  public static final UInt32 ONE = new UInt32(1);

  @Override
  public final @NotNull Integer box() {
    return eval;
  }

  @Contract("_ -> new")
  @Override
  public <L extends Number, V extends Record & Numeral<L, V>> @NotNull UInt32 add(@NotNull V value) {
    return new UInt32(eval + value.box().intValue());
  }

  @Contract("_ -> new")
  @Override
  public <L extends Number, V extends Record & Numeral<L, V>> @NotNull UInt32 div(@NotNull V value) {
    return new UInt32(eval / value.box().intValue());
  }

  @Override
  @Contract(" -> new")
  public final @NotNull UInt32 inc() {
    return new UInt32(eval + 1);
  }
}

