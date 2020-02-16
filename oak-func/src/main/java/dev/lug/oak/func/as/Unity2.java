package dev.lug.oak.func.as;

import dev.lug.oak.func.fun.Function2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Unity2<V1, V2> {
  <T> T as(Function2<V1, V2, T> as);

  @NotNull
  @Contract(value = "_, _ -> new", pure = true)
  static <V1, V2> Unity2<V1, V2> of(final V1 v1, final V2 v2) {
    return new Unity2Impl<>(v1, v2);
  }
}

final class Unity2Impl<V1, V2> implements Unity2<V1, V2> {
  private final V1 v1;
  private final V2 v2;

  @Contract(pure = true)
  Unity2Impl(final V1 v1, final V2 v2) {
    this.v1 = v1;
    this.v2 = v2;
  }

  @Override
  public final <T> T as(@NotNull final Function2<V1, V2, T> as) {
    return as.apply(v1, v2);
  }
}
