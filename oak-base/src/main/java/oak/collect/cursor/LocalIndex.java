package oak.collect.cursor;

import oak.collect.query.Maybe;
import oak.func.sup.AsInt;

public final class LocalIndex implements AsInt {
  private final ThreadLocal<Integer> value;

  private LocalIndex(final ThreadLocal<Integer> value) {
    this.value = value;
  }

  public final int inc() {
    this.value.set(this.value.get() + 1);
    return this.value.get();
  }
  public final int incBy(final int value) {
    this.value.set(this.value.get() + value);
    return this.value.get();
  }

  @Override
  public final int get() {
    return value.get();
  }

  public static Maybe<LocalIndex> of(final int value) {
    return Maybe.of(value)
      .where(it -> it >= 0)
      .select(it -> ThreadLocal.withInitial(() -> it))
      .select(LocalIndex::new);
  }

  public static LocalIndex localIndex(final int value) {
    return of(value).otherwise("Index has a negative value", IllegalArgumentException::new);
  }

  public static LocalIndex zero() { return new LocalIndex(ThreadLocal.withInitial(() -> 0)); }
}
