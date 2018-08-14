package oak.collect.cursor;

final class Once<T> implements Cursor<T> {
  private final T value;
  private final ThreadLocal<Boolean> read;

  Once(final T value) {
    this(value, true);
  }
  private Once(final T value, final boolean read) {
    this(value, ThreadLocal.withInitial(() -> read));
  }
  private Once(final T value, ThreadLocal<Boolean> read) {
    this.value = value;
    this.read = read;
  }

  @Override
  public final boolean hasNext() {
    return read.get();
  }

  @Override
  public final T next() {
    read.set(false);
    return value;
  }
}
