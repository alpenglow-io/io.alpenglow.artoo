package io.artoo.lance.query.cursor;

final class Nothing<R> implements Cursor<R> {
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
