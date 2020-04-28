package io.artoo.func.$2;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import io.artoo.func.Functional;

@FunctionalInterface
public interface FuncInt<T, R> extends Func<Integer, T, R>, Functional.Fun {
  @NotNull
  @Contract(pure = true)
  static <T> FuncInt<T, T> identity() {
    return (index, value) -> value;
  }

  R applyInt(final int param1, final T param2);

  @Override
  default R apply(Integer integer, T t) {
    return applyInt(integer, t);
  }
}
