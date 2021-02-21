package io.artoo.lance.literator.cursor.impl;

import io.artoo.lance.literator.cursor.Cursor;
import io.artoo.lance.literator.cursor.routine.Routine;

import java.util.Iterator;

public final class Iteration<T> implements Cursor<T> {
  private final Iterator<T> iterator;

  public Iteration(final Iterator<T> iterator) {this.iterator = iterator;}

  @Override
  public T fetch() throws Throwable {
    return iterator.next();
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onIterator().apply(iterator);
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }
}
