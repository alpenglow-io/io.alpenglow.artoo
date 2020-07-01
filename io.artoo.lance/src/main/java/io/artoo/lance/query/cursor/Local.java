package io.artoo.lance.query.cursor;

import org.jetbrains.annotations.Contract;

import java.util.Arrays;

import static java.util.Objects.nonNull;

final class Local<R> implements Cursor<R> {
  private R[] elements;
  private Throwable cause;
  private int index;

  Local() {
    this(null, 0, null);
  }

  @SafeVarargs
  Local(R... elements) {
    this(elements, 0, null);
  }

  Local(Throwable cause) {
    this(null, 0, cause);
  }

  @Contract(pure = true)
  private Local(R[] elements, int index, Throwable cause) {
    this.elements = elements;
    this.index = index;
    this.cause = cause;
  }

  @Override
  public final boolean hasNext() {
    return nonNull(elements) && elements.length > 0 && index < elements.length;
  }

  @Override
  public final R next() {
    return hasNext() ? elements[index++] : null;
  }

  @Override
  public Throwable cause() {
    return cause;
  }

  @Override
  public boolean hasCause() {
    return cause != null;
  }

  @SafeVarargs
  @Override
  public final Cursor<R> next(final R... elements) {
    if (elements != null && elements.length > 0) {
      this.elements = elements;
      this.index = 0;
    }
    return this;
  }

  @Override
  public Cursor<R> cause(final Throwable cause) {
    this.elements = null;
    this.cause = cause;
    return this;
  }

  @Override
  public Cursor<R> append(final R element) {
    if (element != null) {
      elements = Arrays.copyOf(elements, elements.length + 1);
      elements[elements.length - 1] = element;
      index = 0;
    }
    return this;
  }

  @Override
  public int size() {
    return elements == null ? 0 : elements.length;
  }
}
