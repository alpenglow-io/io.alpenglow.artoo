package oak.cursor.$2;

import oak.func.$2.Func;
import oak.type.Nullability;
import org.jetbrains.annotations.NotNull;

import static oak.type.Nullability.nonNullable;

final class Once<V1, V2> implements Cursor<V1, V2> {
  private final V1 value1;
  private final V2 value2;
  private boolean notRead;

  Once(final V1 value1, final V2 value2) {
    this.value1 = value1;
    this.value2 = value2;
    this.notRead = true;
  }

  @Override
  public final boolean hasNext() {
    var has = notRead;
    notRead = false;
    return has;
  }

  @Override
  public final <T> T as(@NotNull Func<V1, V2, T> as) {
    return Nullability.nonNullable(as, "as").apply(value1, value2);
  }
}
