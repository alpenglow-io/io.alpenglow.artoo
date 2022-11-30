package io.alpenglow.artoo.lance.literator.cursor;

import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.literator.Pointer;
import io.alpenglow.artoo.lance.literator.cursor.routine.Routine;

public final class Link<T> implements Cursor<T> {
  private final Pointer<T> prev;
  private final Pointer<T> pointer;

  public Link(final Pointer<T> prev, final Pointer<T> pointer) {
    this.prev = prev;
    this.pointer = pointer;
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onLiterator().apply(this);
  }

  @Override
  public T fetch() throws Throwable {
    return prev.hasNext() ? prev.fetch() : pointer.fetch();
  }

  @Override
  public boolean hasNext() {
    return prev.hasNext() || pointer.hasNext();
  }
}
