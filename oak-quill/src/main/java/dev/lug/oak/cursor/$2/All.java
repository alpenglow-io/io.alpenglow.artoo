package dev.lug.oak.cursor.$2;

import dev.lug.oak.cursor.Index;
import dev.lug.oak.func.$2.Fun;
import dev.lug.oak.union.$2.Union;
import org.jetbrains.annotations.NotNull;

import static dev.lug.oak.type.Nullability.nonNullable;

final class All<V1, V2> implements Cursor<V1, V2> {
  private final Union<V1, V2>[] values;
  private final Index index;

  All(final Union<V1, V2>[] values, final Index index) {
    this.values = values;
    this.index = index;
  }

  @Override
  public final boolean hasNext() {
    return this.index.eval() < this.values.length;
  }

  @Override
  public final <T> T as(@NotNull Fun<V1, V2, T> as) {
    return this.values[this.index.eval()].as(nonNullable(as, "as"));
  }
}
