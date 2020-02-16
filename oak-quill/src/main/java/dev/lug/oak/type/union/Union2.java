package dev.lug.oak.type.union;

import dev.lug.oak.func.fun.Function2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface Union2<V1, V2> {
  <T> T as(Function2<V1, V2, T> as);

  @NotNull
  @Contract(value = "_, _ -> new", pure = true)
  static <V1, V2> Union2<V1, V2> of(final V1 v1, final V2 v2) {
    return new Union2Impl<>(v1, v2);
  }
}

final class Union2Impl<V1, V2> implements Union2<V1, V2> {
  private final V1 v1;
  private final V2 v2;

  @Contract(pure = true)
  Union2Impl(final V1 v1, final V2 v2) {
    this.v1 = v1;
    this.v2 = v2;
  }

  @Override
  public final <T> T as(@NotNull final Function2<V1, V2, T> as) {
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
    var unity2 = (Union2Impl<?, ?>) o;
    return Objects.equals(v1, unity2.v1) && Objects.equals(v2, unity2.v2);
  }

  @Override
  public int hashCode() {
    int result = v1 != null ? v1.hashCode() : 0;
    result = 31 * result + (v2 != null ? v2.hashCode() : 0);
    return result;
  }
}
