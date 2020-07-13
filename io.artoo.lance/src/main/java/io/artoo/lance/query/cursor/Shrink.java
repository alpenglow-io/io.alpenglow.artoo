package io.artoo.lance.query.cursor;

final class Shrink<T> implements Cursor<T> {
  private final Cursor<T> cursor;
  private final Shrunk shrunk;

  Shrink(final Cursor<T> cursor) {
    this.cursor = cursor;
    this.shrunk = new Shrunk();
  }

  @Override
  public T fetch() {
    return next();
  }

  @Override
  public boolean hasNext() {
    var hasNext = cursor.hasNext();
    shrunk.fetched = null;
    while (hasNext && shrunk.fetched == null) {
      shrunk.fetched = cursor.next();
      hasNext = shrunk.fetched != null;
    }
    return hasNext;
  }

  @Override
  public T next() {
    return shrunk.fetched();
  }

  private final class Shrunk {
    private T fetched;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private T fetched() {
      if (fetched == null) hasNext();
      return fetched;
    }
  }
}
