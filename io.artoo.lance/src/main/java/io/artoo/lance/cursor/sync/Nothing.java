package io.artoo.lance.cursor.sync;

import io.artoo.lance.cursor.Cursor;

public final class Nothing<R> implements Cursor<R> {
  @Override
  public R fetch() {
    return null;
  }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public R next() {
    return null;
  }
}
