package io.artoo.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Int64(long eval) implements Numeral<Long, Int64> {
  public static final Int64 ZERO = new Int64(0L);
  public static final Int64 ONE = new Int64(1L);

  @Override
  public final @NotNull Long box() {
    return eval;
  }

  @Override
  @Contract("_ -> new")
  public final @NotNull Int64 add(@NotNull Int64 value) {
    return new Int64(eval + value.eval);
  }
}

