package artoo.union.$3;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import artoo.func.$3.Func;

@FunctionalInterface
public interface Union<V1, V2, V3> {
  @NotNull
  @Contract(value = "_, _, _ -> new", pure = true)
  static <V1, V2, V3> Union<V1, V2, V3> of(final V1 value1, final V2 value2, final V3 value3) {
    return new UnionImpl<>(value1, value2, value3);
  }

  <T> T as(Func<V1, V2, V3, T> as);
}

