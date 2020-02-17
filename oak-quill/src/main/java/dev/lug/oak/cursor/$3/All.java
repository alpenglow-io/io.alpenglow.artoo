package dev.lug.oak.cursor.$3;

import dev.lug.oak.cursor.Index;
import dev.lug.oak.func.$3.Fun;
import dev.lug.oak.union.$3.Union;
import org.jetbrains.annotations.NotNull;

import static dev.lug.oak.type.Nullability.nonNullable;

final class All<V1, V2, V3> implements Cursor<V1, V2, V3> {
  private final Union<V1, V2, V3>[] unions;
  private final Index index;

  All(final Union<V1, V2, V3>[] unions, final Index index) {
    this.unions = unions;
    this.index = index;
  }

  @Override
  public final boolean hasNext() {
    return this.index.eval() < this.unions.length;
  }

  @Override
  public final <T> T as(@NotNull final Fun<V1, V2, V3, T> as) {
    return this.unions[this.index.eval()].as(nonNullable(as, "as"));
  }
}
