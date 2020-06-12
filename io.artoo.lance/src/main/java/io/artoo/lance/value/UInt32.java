package io.artoo.lance.value;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.query.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public record UInt32(One<UInt32> one) implements One<UInt32>, Numeric<UInt32> {
  public UInt32(int eval) {
    assert eval >= 0;
    this(One.just(eval));
  }

  public static final UInt32 ZERO = new UInt32(0);
  public static final UInt32 ONE = new UInt32(1);

  public static UInt32 let(final int value) {
    return new UInt32(value);
  }

  public int eval() {
    final var cursor = one.cursor();
    return cursor.hasNext() ? cursor.next().eval : eval;
  }

  @NotNull
  @Override
  public Iterator<UInt32> iterator() {
    final var cursor = one.cursor();
    return cursor.hasNext() ? cursor : Cursor.just(this);
  }

  @Override
  public final @NotNull Integer raw() {
    return eval();
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeric<V>> @NotNull UInt32 add(@NotNull V value) {
    return new UInt32(eval() + value.raw().intValue());
  }

  @Contract("_ -> new")
  @Override
  public <V extends Record & Numeric<V>> @NotNull UInt32 div(@NotNull V value) {
    return new UInt32(eval / value.raw().intValue());
  }

  @Override
  @Contract(" -> new")
  public final @NotNull UInt32 inc() {
    return new UInt32(eval + 1);
  }
}

