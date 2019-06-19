package oak.collect.cursor;

import oak.collect.query.Maybe;
import oak.type.AsInt;

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
  public final int get() {
    return value.get();
  }

  public static Maybe<LocalIndex> of(final int value) {
    return Maybe.of(value)
      .where(it -> it >= 0)
      .select(it -> new LocalIndex(withInitial(() -> it)));
  }

  public static LocalIndex localIndex(final int value) {
    return of(value).otherwise("Index has a negative value", IllegalArgumentException::new);
  }

  public static LocalIndex zero() { return new LocalIndex(withInitial(() -> 0)); }
}
