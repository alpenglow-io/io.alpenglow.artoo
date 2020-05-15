package io.artoor.cursor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public enum Default implements Cursor<Record> {
  None;

  @Override
  public final boolean hasNext() {
    return false;
  }

  @Nullable
  @Contract(pure = true)
  @Override
  public final Record next() {
    return null;
  }

  @Override
  public final Cursor<Record> resume() {
    return this;
  }
}
