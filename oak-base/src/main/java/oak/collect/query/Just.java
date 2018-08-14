package oak.collect.query;

final class Just<T> implements Maybe<T> {
  private final T value;

  Just(final T value) {
    this.value = value;
  }

  @Override
  public final T get() {
    return value;
  }
}
