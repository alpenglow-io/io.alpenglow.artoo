package io.artoo.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Int32(int eval) implements Numeral<Integer, Int32> {
  public static final Int32 ZERO = new Int32(0);
  public static final Int32 ONE = new Int32(1);

  @Override
  public final @NotNull Integer box() {
    return eval;
  }

  @Override
  @Contract("_ -> new")
  public final @NotNull Int32 add(@NotNull Int32 value) {
    return new Int32(eval + value.eval);
  }
}

