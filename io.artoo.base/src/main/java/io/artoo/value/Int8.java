package io.artoo.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Int8(byte eval) implements Numeral<Byte, Int8> {
  public static final Int8 ZERO = new Int8((byte) 0);
  public static final Int8 ONE = new Int8((byte) 1);

  @Override
  public final @NotNull Byte box() {
    return eval;
  }

  @Contract("_ -> new")
  @Override
  public <L extends Number, V extends Record & Numeral<L, V>> @NotNull Int8 add(@NotNull V value) {
    return new Int8((byte) (eval + value.box().byteValue()));
  }

  @Contract("_ -> new")
  @Override
  public <L extends Number, V extends Record & Numeral<L, V>> @NotNull Int8 div(@NotNull V value) {
    return new Int8((byte) (eval / value.box().byteValue()));
  }

  @Override
  @Contract(" -> new")
  public final @NotNull Int8 inc() {
    return new Int8((byte) (eval + 1));
  }
}


