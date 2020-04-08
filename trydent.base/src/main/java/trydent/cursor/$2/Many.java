package trydent.cursor.$2;

import trydent.cursor.Index;
import trydent.func.$2.Func;
import trydent.union.$2.Union;
import trydent.type.Nullability;
import org.jetbrains.annotations.NotNull;

import static trydent.type.Nullability.nonNullable;

final class Many<V1, V2> implements Cursor<V1, V2> {
  private final Union<V1, V2>[] values;
  private final Index index;

  Many(final Union<V1, V2>[] values, final Index index) {
    this.values = values;
    this.index = index;
  }

  @Override
  public final boolean hasNext() {
    return this.index.eval() < this.values.length;
  }

  @Override
  public final <T> T as(@NotNull Func<V1, V2, T> as) {
    return this.values[this.index.eval()].as(Nullability.nonNullable(as, "as"));
  }
}
