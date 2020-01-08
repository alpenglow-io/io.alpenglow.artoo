package dev.lug.oak.collect.cursor;

import dev.lug.oak.query.one.One;
import dev.lug.oak.type.AsInt;

import static java.lang.ThreadLocal.withInitial;

public final class LocalIndex implements AsInt {
  private final ThreadLocal<Integer> value;

  private LocalIndex(final ThreadLocal<Integer> value) {
    this.value = value;
  }

  public final int incThenGet() {
    this.value.set(this.value.get() + 1);
    return this.value.get();
  }
  public final int getThenInc() {
    final var current = this.value.get();
    this.value.set(this.value.get() + 1);
    return current;
  }

  @Override
  public final int eval() {
    return value.get();
  }

  public static One<LocalIndex> of(final int value) {
    return One.of(value)
      .where(it -> it >= 0)
      .select(it -> new LocalIndex(withInitial(() -> it)));
  }

  public static LocalIndex zero() { return new LocalIndex(withInitial(() -> 0)); }
}
