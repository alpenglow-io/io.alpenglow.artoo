package artoo.cursor.$3;

import org.jetbrains.annotations.NotNull;
import artoo.cursor.Index;
import artoo.func.$3.Func;
import artoo.type.Nullability;
import artoo.union.$3.Union;

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
  public final <T> T as(@NotNull final Func<V1, V2, V3, T> as) {
    return this.unions[this.index.eval()].as(Nullability.nonNullable(as, "as"));
  }
}
