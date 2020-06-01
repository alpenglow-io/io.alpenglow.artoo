package io.artoo.lance.cursor;

final class None<T extends Record> implements Cursor<T> {
  @Override
  public final boolean hasNext() {
    return false;
  }

  @Override
  public final T next() {
    return null;
  }

  @Override
  public final Cursor<T> resume() { return this; }
}
