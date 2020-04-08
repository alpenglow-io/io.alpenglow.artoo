package trydent.union.$2;

import trydent.func.$2.Func;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

final class UnionImpl<V1, V2> implements Union<V1, V2> {
  private final V1 v1;
  private final V2 v2;

  @Contract(pure = true)
  UnionImpl(final V1 v1, final V2 v2) {
    this.v1 = v1;
    this.v2 = v2;
  }

  @Override
  public final <T> T as(@NotNull final Func<V1, V2, T> as) {
    return as.apply(v1, v2);
  }

  @Override
  public String toString() {
    return String.format("Unity2 { v1=%s, v2=%s }", v1, v2);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    var unity2 = (UnionImpl<?, ?>) o;
    return Objects.equals(v1, unity2.v1) && Objects.equals(v2, unity2.v2);
  }

  @Override
  public int hashCode() {
    int result = v1 != null ? v1.hashCode() : 0;
    result = 31 * result + (v2 != null ? v2.hashCode() : 0);
    return result;
  }
}
