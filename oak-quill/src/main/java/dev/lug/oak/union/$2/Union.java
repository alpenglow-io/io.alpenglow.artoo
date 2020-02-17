package dev.lug.oak.union.$2;

import dev.lug.oak.func.$2.Fun;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface Union<V1, V2> {
  <T> T as(Fun<V1, V2, T> as);

  @NotNull
  @Contract(value = "_, _ -> new", pure = true)
  static <V1, V2> Union<V1, V2> of(final V1 v1, final V2 v2) {
    return new UnionImpl<>(v1, v2);
  }
}

