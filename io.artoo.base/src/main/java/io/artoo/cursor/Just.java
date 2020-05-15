package io.artoo.cursor;

import org.jetbrains.annotations.Contract;

final class Just<T extends Record> implements Cursor<T> {
  private final T value;
  private final ThreadLocal<Boolean> notRead;

  Just(final T value) {
    this(value, ThreadLocal.withInitial(() -> true));
  }

  @Contract(pure = true)
  private Just(final T value, ThreadLocal<Boolean> notRead) {
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

  @Override
  public final Cursor<T> resume() {
    if (!notRead.get()) notRead.set(true);
    return this;
  }
}
