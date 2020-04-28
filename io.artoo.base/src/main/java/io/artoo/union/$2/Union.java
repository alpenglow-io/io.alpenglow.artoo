package io.artoo.union.$2;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import io.artoo.func.$2.Func;

public interface Union<V1, V2> {
  @NotNull
  @Contract(value = "_, _ -> new", pure = true)
  static <V1, V2> Union<V1, V2> of(final V1 v1, final V2 v2) {
    return new UnionImpl<>(v1, v2);
  }

  <T> T as(Func<V1, V2, T> as);
}

