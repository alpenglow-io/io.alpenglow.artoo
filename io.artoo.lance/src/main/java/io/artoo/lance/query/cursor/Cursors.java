package io.artoo.lance.query.cursor;

import java.util.Arrays;

import static java.lang.System.arraycopy;

public interface Cursors<T> {
  boolean isEmpty();
  default boolean isNotEmpty() { return !isEmpty(); }

  Cursors<T> attach(Cursor<T> cursor);
  Cursor<T> detach();
  Cursor<T> peek();

  @SafeVarargs
  static <T> Cursors<T> queued(final Cursor<T>... cursors) {
    return new Queued<>(cursors);
  }
}

@SuppressWarnings("unchecked")
final class Queued<T> implements Cursors<T> {
  private Cursor<T>[] elements;

  Queued(final Cursor<T>... elements) {this.elements = elements;}

  @Override
  public boolean isEmpty() {
    return elements.length == 0;
  }

  @Override
  public Cursors<T> attach(final Cursor<T> element) {
    elements = Arrays.copyOf(elements, elements.length + 1);
    elements[elements.length - 1] = element;
    return this;
  }

  @Override
  public Cursor<T> detach() {
    if (isNotEmpty()) {
      final var element = elements[0];
      var items = new Cursor[elements.length - 1];
      arraycopy(elements, 1, items, 0, items.length);
      elements = (Cursor<T>[]) items;
      return element;
    }
    return null;
  }

  @Override
  public Cursor<T> peek() {
    return isNotEmpty() ? elements[0] : null;
  }
}
