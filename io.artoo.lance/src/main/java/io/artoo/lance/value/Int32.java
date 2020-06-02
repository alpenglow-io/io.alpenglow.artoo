package io.artoo.lance.value;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.query.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public record Int32(int eval) implements Numeral<Int32>, One<Int32> {
  public static final Int32 ZERO = new Int32(0);
  public static final Int32 ONE = new Int32(1);

  @Contract("_ -> new")
  public static @NotNull Int32 let(final int value) {
    return new Int32(value);
  }

  public Decimal64 asSingle64() {
    return new Decimal64(eval);
  }

  @Override
  public final @NotNull Integer raw() {
    return eval;
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeral<V>> @NotNull Int32 add(@NotNull V value) {
    return new Int32(eval + value.raw().intValue());
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeral<V>> @NotNull Int32 div(@NotNull V value) {
    return new Int32(eval / value.raw().intValue());
  }

  @Override
  @Contract(" -> new")
  public final @NotNull Int32 inc() {
    return new Int32(eval + 1);
  }

  @NotNull
  @Override
  public final Iterator<Int32> iterator() {
    return Cursor.just(this);
  }
}

