package io.artoo.lance.cursor;

import java.util.Arrays;

import static java.lang.System.arraycopy;

public interface Cursors<T> {
  boolean isEmpty();
  default boolean isNotEmpty() { return !isEmpty(); }

  Cursors<T> attach(Cursor<T> cursor);
  Cursor<T> detach();
  Cursor<T> peek();

  @SafeVarargs
  static <T> Cursors<T> enqueue(final Cursor<T>... cursors) {
    return new Enqueue<>(cursors);
  }
}

