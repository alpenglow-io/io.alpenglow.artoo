package artoo.cursor;

import org.jetbrains.annotations.Contract;

final class Just<T> implements Cursor<T> {
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
  public final void resume() {
    if (!notRead.get()) notRead.set(true);
  }
}
