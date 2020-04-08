package trydent.union;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Union {
  @NotNull
  @Contract(pure = true)
  static <V1, V2> trydent.union.$2.Union<V1, V2> of(final V1 value1, final V2 value2) {
    return trydent.union.$2.Union.of(value1, value2);
  }

  @NotNull
  @Contract(pure = true)
  static <V1, V2, V3> trydent.union.$3.Union<V1, V2, V3> of(final V1 value1, final V2 value2, final V3 value3) {
    return trydent.union.$3.Union.of(value1, value2, value3);
  }
}
