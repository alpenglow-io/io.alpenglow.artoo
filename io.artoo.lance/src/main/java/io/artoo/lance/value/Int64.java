package io.artoo.lance.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Int64(long eval) implements Numeric<Int64> {
  public static final Int64 ZERO = new Int64(0L);
  public static final Int64 ONE = new Int64(1L);

  public static Int64 let(final long value) {
    return new Int64(value);
  }

  @Override
  public final @NotNull Long raw() {
    return eval;
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeric<V>> @NotNull Int64 add(@NotNull V value) {
    return new Int64(eval + value.raw().longValue());
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeric<V>> @NotNull Int64 div(@NotNull V value) {
    return new Int64(eval / value.raw().longValue());
  }

  @Override
  @Contract(" -> new")
  public final @NotNull Int64 inc() {
    return new Int64(eval + 1);
  }
}

