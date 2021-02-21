package io.artoo.lance.literator.cursor.impl;

import io.artoo.lance.literator.cursor.Cursor;
import io.artoo.lance.literator.cursor.routine.Routine;

public enum Nothing implements Cursor<Object> {
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
    return routine.onPlain().apply(null);
  }
}
