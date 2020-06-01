package io.artoo.lance.cursor;

import io.artoo.lance.type.AsInt;

import static java.lang.ThreadLocal.withInitial;

public final class LocalIndex implements AsInt {
  private final ThreadLocal<Integer> value;

  private LocalIndex(final ThreadLocal<Integer> value) {
    this.value = value;
  }

  public static LocalIndex zero() { return new LocalIndex(withInitial(() -> 0)); }

  public final int incThenGet() {
    this.value.set(this.value.get() + 1);
    return this.value.get();
  }

  public final int getThenInc() {
    final var current = this.value.get();
    this.value.set(this.value.get() + 1);
    return current;
  }

/*
  public static One<LocalIndex> of(final int value) {
    return One.of(value)
      .where(it -> it >= 0)
      .select(Q.P.just(it -> new LocalIndex(withInitial(() -> it))));
  }
*/

  @Override
  public final int eval() {
    return value.get();
  }
}
