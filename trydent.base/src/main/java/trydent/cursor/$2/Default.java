package trydent.cursor.$2;

import trydent.func.$2.Func;
import trydent.union.$2.Union;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

enum Default implements Cursor<Object, Object> {
  None;

  @Override
  public boolean hasNext() {
    return false;
  }

  @Nullable
  @Contract(pure = true)
  @Override
  public Union<Object, Object> next() {
    return null;
  }

  @Override
  public <T> T as(@NotNull final Func<Object, Object, T> as) {
    return as.apply(null, null);
  }
}
