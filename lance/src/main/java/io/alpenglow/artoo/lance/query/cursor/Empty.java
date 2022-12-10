package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

public enum Empty implements Cursor<Object> {
  Default;

  @Override
  public Object fetch() {
    return null;
  }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public <R> R as(final Routine<Object, R> routine) {
    return routine.onSelf().apply(null);
  }
}
