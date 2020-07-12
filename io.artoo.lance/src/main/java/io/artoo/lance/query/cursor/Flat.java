package io.artoo.lance.query.cursor;

@SuppressWarnings("StatementWithEmptyBody")
public class Flat<T> implements Cursor<T> {
  private final Cursor<Cursor<T>> cursor;
  private final Flatten flatten;

  public Flat(final Cursor<Cursor<T>> cursor) {
    assert cursor != null;
    this.cursor = cursor;
    this.flatten = new Flatten();
  }

  @Override
  public final T fetch() throws Throwable {
    T element = null;
    while (hasNext() && (element = flatten.cursor.fetch()) == null);
    return element;
  }

  @Override
  public boolean hasNext() {
    flatten.hasNext |= cursor.hasNext();

    if (flatten.hasNext) {
      if (flatten.cursor == null || !flatten.cursor.hasNext()) {
        flatten.cursor = cursor.next();
        flatten.hasNext = flatten.cursor.hasNext();
      }
    }

    return flatten.hasNext;
  }

  @Override
  public T next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      return null;
    }
  }

  private final class Flatten {
    private boolean hasNext = true;
    private Cursor<T> cursor;
  }
}
