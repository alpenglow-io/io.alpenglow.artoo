package io.artoo.lance.literator.cursor.impl;

import io.artoo.lance.literator.cursor.Cursor;
import io.artoo.lance.literator.Literator;
import io.artoo.lance.literator.cursor.routine.Routine;

public final class Link<T> implements Cursor<T> {
  private final Literator<T> prev;
  private final Literator<T> next;

  public Link(final Literator<T> prev, final Literator<T> next) {
    this.prev = prev;
    this.next = next;
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onLiterator().apply(this);
  }

  @Override
  public T fetch() throws Throwable {
    return prev.hasNext() ? prev.fetch() : next.fetch();
  }

  @Override
  public boolean hasNext() {
    return prev.hasNext() || next.hasNext();
  }
}
