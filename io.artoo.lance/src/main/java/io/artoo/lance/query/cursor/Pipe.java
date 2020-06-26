package io.artoo.lance.query.cursor;

import org.jetbrains.annotations.Contract;

import java.util.Arrays;

import static java.util.Objects.nonNull;

final class Pipe<R> implements Cursor<R> {
  private R[] elements;
  private int index;

  @SafeVarargs
  Pipe(R... elements) {
    this(elements, 0);
  }

  @Contract(pure = true)
  private Pipe(R[] elements, int index) {
    this.elements = elements;
    this.index = index;
  }

  @Override
  public final boolean hasNext() {
    return nonNull(elements) && elements.length > 0 && index < elements.length;
  }

  @Override
  public final R next() {
    return elements.length > 0 && index < elements.length ? elements[index++] : null;
  }

  @Override
  public Cursor<R> append(final R element) {
    if (element != null) {
      elements = Arrays.copyOf(elements, elements.length + 1);
      elements[elements.length - 1] = element;
    }
    return this;
  }

  @Override
  public int size() {
    return elements.length;
  }
}
