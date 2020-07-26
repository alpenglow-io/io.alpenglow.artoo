package io.artoo.lance.cursor.sync;

import io.artoo.lance.cursor.Cursor;

import java.util.Iterator;

public final class Iteration<T> implements Cursor<T> {
  private final Iterator<T> iterator;

  public Iteration(final Iterator<T> Iterator) {
    iterator = Iterator;
  }

  @Override
  public T fetch() {
    return iterator.next();
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public T next() {
    return fetch();
  }
}
