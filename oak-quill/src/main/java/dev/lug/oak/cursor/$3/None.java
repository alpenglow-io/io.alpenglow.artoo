package dev.lug.oak.cursor.$3;

import dev.lug.oak.func.$3.Fun;
import org.jetbrains.annotations.NotNull;

import static dev.lug.oak.type.Nullability.nonNullable;

final class None<V1, V2, V3> implements Cursor<V1, V2, V3> {
  @Override
  public final <T> T as(@NotNull Fun<V1, V2, V3, T> as) {
    return nonNullable(as, "as").apply(null, null, null);
  }
}
