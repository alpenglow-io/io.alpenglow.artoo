package oak.cursor.$3;

import oak.cursor.Index;
import oak.func.$3.Fun;
import oak.union.$3.Union;
import oak.type.Nullability;
import org.jetbrains.annotations.NotNull;

import static oak.type.Nullability.nonNullable;

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
    return this.unions[this.index.eval()].as(Nullability.nonNullable(as, "as"));
  }
}
