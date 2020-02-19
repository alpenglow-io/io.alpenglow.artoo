package oak.cursor.$3;

import oak.func.$3.Fun;
import oak.type.Nullability;
import org.jetbrains.annotations.NotNull;

import static oak.type.Nullability.nonNullable;

final class None<V1, V2, V3> implements Cursor<V1, V2, V3> {
  @Override
  public final <T> T as(@NotNull Fun<V1, V2, V3, T> as) {
    return Nullability.nonNullable(as, "as").apply(null, null, null);
  }
}
