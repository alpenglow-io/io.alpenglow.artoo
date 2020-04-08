package trydent.cursor.$2;

import trydent.func.$2.Func;
import trydent.type.Nullability;
import org.jetbrains.annotations.NotNull;

import static trydent.type.Nullability.nonNullable;

final class None<V1, V2> implements Cursor<V1, V2> {
  @Override
  public final <T> T as(@NotNull Func<V1, V2, T> as) {
    return Nullability.nonNullable(as, "as").apply(null, null);
  }

  @Override
  public final boolean hasNext() {
    return false;
  }
}
