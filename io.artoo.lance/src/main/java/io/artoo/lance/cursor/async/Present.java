package io.artoo.lance.cursor.async;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.cursor.Hand;

public final class Present<T> implements Hand<T> {
  private final Cursor<T> cursor;

  public Present(final Cursor<T> cursor) {this.cursor = cursor;}

  @Override
  public T fetch() throws Throwable {
    return cursor.fetch();
  }

  @Override
  public boolean hasNext() {
    return cursor.hasNext();
  }

  @Override
  public T next() {
    return cursor.next();
  }
}
