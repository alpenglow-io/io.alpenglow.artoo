package artoo.cursor.$3;

import org.jetbrains.annotations.NotNull;
import artoo.func.$3.Func;
import artoo.type.Nullability;

final class None<V1, V2, V3> implements Cursor<V1, V2, V3> {
  @Override
  public final <T> T as(@NotNull Func<V1, V2, V3, T> as) {
    return Nullability.nonNullable(as, "as").apply(null, null, null);
  }
}
