package oak.collect.cursor;

import java.util.Iterator;

final class Jump<T> implements Cursor<T> {
  private final Iterator<T> first;
  private final Iterator<T> second;

  Jump(final Iterator<T> first, final Iterator<T> second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public final boolean hasNext() {
    return first.hasNext() || second.hasNext();
  }

  @Override
  public final T next() {
    return first.hasNext()
      ? first.next()
      : second.next();
  }
}
