package io.artoor.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Int64(long eval) implements Numeral<Long, Int64> {
  public static final Int64 ZERO = new Int64(0L);
  public static final Int64 ONE = new Int64(1L);

  @Override
  public final @NotNull Long box() {
    return eval;
  }

  @Contract("_ -> new")
  @Override
  public <L extends Number, V extends Record & Numeral<L, V>> @NotNull Int64 add(@NotNull V value) {
    return new Int64(eval + value.box().longValue());
  }

  @Contract("_ -> new")
  @Override
  public <L extends Number, V extends Record & Numeral<L, V>> @NotNull Int64 div(@NotNull V value) {
    return new Int64(eval / value.box().longValue());
  }

  @Override
  @Contract(" -> new")
  public final @NotNull Int64 inc() {
    return new Int64(eval + 1);
  }
}

