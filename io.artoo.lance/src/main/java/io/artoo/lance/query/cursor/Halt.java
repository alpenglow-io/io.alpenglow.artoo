package io.artoo.lance.query.cursor;

record Halt<R>(Throwable cause) implements Cursor<R> {
  public Halt { assert cause != null; }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public R next() {
    return null;
  }

  @Override
  public Cursor<R> append(final R element) {
    return Cursor.of(element);
  }

  @Override
  public boolean hasHalted() {
    return true;
  }
}
