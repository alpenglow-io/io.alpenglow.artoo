package dev.lug.oak.cursor.$2;

import dev.lug.oak.func.$2.Fun;
import org.jetbrains.annotations.NotNull;

import static dev.lug.oak.type.Nullability.nonNullable;

final class None<V1, V2> implements Cursor<V1, V2> {
  @Override
  public final <T> T as(@NotNull Fun<V1, V2, T> as) {
    return nonNullable(as, "as").apply(null, null);
  }

  @Override
  public final boolean hasNext() {
    return false;
  }
}
