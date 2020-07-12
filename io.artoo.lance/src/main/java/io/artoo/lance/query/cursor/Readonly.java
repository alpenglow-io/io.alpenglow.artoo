package io.artoo.lance.query.cursor;

import static java.util.Objects.nonNull;

@SuppressWarnings("StatementWithEmptyBody")
final class Readonly<T> implements Cursor<T> {
  private static final class Index {
    private int value = 0;
  }

  private final Index index = new Index();
  private final T[] elements;

  @SafeVarargs
  Readonly(T... elements) {
    this.elements = elements;
  }

  @Override
  public final boolean hasNext() {
    return nonNull(elements) && elements.length > 0 && index.value < elements.length;
  }

  @Override
  public final T next() {
    T element = null;
    while (hasNext() && (element = elements[index.value++]) == null);
    return element;
  }

  @Override
  public final T fetch() {
    return next();
  }
}
