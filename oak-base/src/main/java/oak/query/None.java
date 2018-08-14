package oak.query;

final class None<T> implements Maybe<T> {
  @Override
  public final T get() {
    return null;
  }
}
