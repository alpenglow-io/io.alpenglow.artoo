package dev.lug.oak.collect.cursor;

import org.jetbrains.annotations.Contract;

final class Once<T> implements Cursor<T> {
  private final T value;
  private final ThreadLocal<Boolean> notRead;

  Once(final T value) {
    this(value, ThreadLocal.withInitial(() -> true));
  }
  @Contract(pure = true)
  private Once(final T value, ThreadLocal<Boolean> notRead) {
    this.value = value;
    this.notRead = notRead;
  }

  @Override
  public final boolean hasNext() {
    return notRead.get();
  }

  @Override
  public final T next() {
    notRead.set(false);
    return value;
  }
}
