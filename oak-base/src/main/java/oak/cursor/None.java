package oak.cursor;

final class None<T> implements Cursor<T> {
  @Override
  public final boolean hasNext() {
    return false;
  }

  @Override
  public final T next() {
    return null;
  }
}
