package oak.union.$3;

import oak.func.$3.Fun;
import oak.type.Nullability;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static oak.type.Nullability.nonNullable;

final class UnionImpl<V1, V2, V3> implements Union<V1, V2, V3> {
  private final V1 value1;
  private final V2 value2;
  private final V3 value3;

  UnionImpl(final V1 value1, final V2 value2, final V3 value3) {
    this.value1 = value1;
    this.value2 = value2;
    this.value3 = value3;
  }

  @Override
  public final <T> T as(@NotNull final Fun<V1, V2, V3, T> as) {
    return Nullability.nonNullable(as, "as").apply(value1, value2, value3);
  }

  @Override
  public String toString() {
    return String.format("Union3 { value1=%s, value2=%s, value3=%s }", value1, value2, value3);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    var union3 = (UnionImpl<?, ?, ?>) o;
    return Objects.equals(value1, union3.value1) && Objects.equals(value2, union3.value2) && Objects.equals(value3, union3.value3);
  }

  @Override
  public int hashCode() {
    var result = value1 != null ? value1.hashCode() : 0;
    result = 31 * result + (value2 != null ? value2.hashCode() : 0);
    result = 31 * result + (value3 != null ? value3.hashCode() : 0);
    return result;
  }
}
