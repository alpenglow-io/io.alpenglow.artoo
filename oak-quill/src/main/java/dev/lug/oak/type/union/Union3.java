package dev.lug.oak.type.union;

import dev.lug.oak.func.fun.Function3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static dev.lug.oak.type.Nullability.nonNullable;

@FunctionalInterface
public interface Union3<V1, V2, V3> {
  <T> T as(Function3<V1, V2, V3, T> as);
}

final class Union3Impl<V1, V2, V3> implements Union3<V1, V2, V3> {
  private final V1 value1;
  private final V2 value2;
  private final V3 value3;

  Union3Impl(final V1 value1, final V2 value2, final V3 value3) {
    this.value1 = value1;
    this.value2 = value2;
    this.value3 = value3;
  }

  @Override
  public final <T> T as(@NotNull final Function3<V1, V2, V3, T> as) {
    return nonNullable(as, "as").apply(value1, value2, value3);
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

    var union3 = (Union3Impl<?, ?, ?>) o;
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
