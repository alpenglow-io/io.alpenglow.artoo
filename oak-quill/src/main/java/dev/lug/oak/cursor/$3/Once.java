package dev.lug.oak.cursor.$3;

import dev.lug.oak.func.$3.Fun;
import org.jetbrains.annotations.NotNull;

import static dev.lug.oak.type.Nullability.nonNullable;

final class Once<V1, V2, V3> implements Cursor<V1, V2, V3> {
  private final V1 value1;
  private final V2 value2;
  private final V3 value3;
  private boolean notRead;

  Once(final V1 value1, final V2 value2, final V3 value3) {
    this.value1 = value1;
    this.value2 = value2;
    this.value3 = value3;
    this.notRead = true;
  }

  @Override
  public final boolean hasNext() {
    var has = notRead;
    notRead = false;
    return has;
  }

  @Override
  public final <T> T as(@NotNull Fun<V1, V2, V3, T> as) {
    return nonNullable(as, "as").apply(value1, value2, value3);
  }
}
