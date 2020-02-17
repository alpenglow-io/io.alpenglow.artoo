package dev.lug.oak.union;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Union {
  @NotNull
  @Contract(pure = true)
  static <V1, V2> dev.lug.oak.union.$2.Union<V1, V2> of(final V1 value1, final V2 value2) {
    return dev.lug.oak.union.$2.Union.of(value1, value2);
  }

  @NotNull
  @Contract(pure = true)
  static <V1, V2, V3>dev.lug.oak.union.$3.Union<V1, V2, V3> of(final V1 value1, final V2 value2, final V3 value3) {
    return dev.lug.oak.union.$3.Union.of(value1, value2, value3);
  }
}
