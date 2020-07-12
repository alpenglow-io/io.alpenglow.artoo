package io.artoo.lance.query.cursor;

@SuppressWarnings("StatementWithEmptyBody")
final class Concat<T> implements Cursor<T> {
  private final Cursor<T> start;
  private final Cursor<T> end;
  private final Concatenated concatenated;

  Concat(final Cursor<T> start, final Cursor<T> end) {
    this.start = start;
    this.end = end;
    this.concatenated = new Concatenated();
  }

  @Override
  public final T fetch() throws Throwable {
    T element = null;
    while (hasNext() && (element = concatenated.cursor.fetch()) == null);
    return element;
  }

  @Override
  public boolean hasNext() {
    concatenated.hasNext &= start.hasNext() || end.hasNext();

    if (start.hasNext()) {
      concatenated.cursor = start;
    } else {
      concatenated.cursor = end;
    }

    return concatenated.hasNext;
  }

  @Override
  public T next() {
    return hasNext() ? concatenated.cursor.next() : null;
  }

  private final class Concatenated {
    private boolean hasNext = true;
    private Cursor<T> cursor;
  }
}
