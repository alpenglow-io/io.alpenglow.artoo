package dev.lug.oak.union.$3;

import dev.lug.oak.func.$3.Fun;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static dev.lug.oak.type.Nullability.nonNullable;

@FunctionalInterface
public interface Union<V1, V2, V3> {
  <T> T as(Fun<V1, V2, V3, T> as);

  @NotNull
  @Contract(value = "_, _, _ -> new", pure = true)
  static <V1, V2, V3> Union<V1, V2, V3> of(final V1 value1, final V2 value2, final V3 value3) {
    return new UnionImpl<>(value1, value2, value3);
  }
}

