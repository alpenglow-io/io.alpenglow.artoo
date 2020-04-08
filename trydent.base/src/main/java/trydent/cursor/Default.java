package trydent.cursor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

enum Default implements Cursor<Object> {
  None;

  @Override
  public boolean hasNext() {
    return false;
  }

  @Nullable
  @Contract(pure = true)
  @Override
  public Object next() {
    return null;
  }
}
