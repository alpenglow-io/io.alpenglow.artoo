package oak.union;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Union {
  @NotNull
  @Contract(pure = true)
  static <V1, V2> oak.union.$2.Union<V1, V2> of(final V1 value1, final V2 value2) {
    return oak.union.$2.Union.of(value1, value2);
  }

  @NotNull
  @Contract(pure = true)
  static <V1, V2, V3> oak.union.$3.Union<V1, V2, V3> of(final V1 value1, final V2 value2, final V3 value3) {
    return oak.union.$3.Union.of(value1, value2, value3);
  }
}
