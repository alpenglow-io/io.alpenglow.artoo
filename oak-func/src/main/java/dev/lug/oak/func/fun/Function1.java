package dev.lug.oak.func.fun;

import dev.lug.oak.func.Functional;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Function1<T, R> extends Function<T, R>, Functional.Fun {
  default <V> Function<V, R> before(Function<? super V, ? extends T> before) {
    return compose(requireNonNull(before, "Before is null"));
  }

  default <V> Function<T, V> after(Function<? super R, ? extends V> after) {
    final var nonNull = requireNonNull(after, "After is null");
    return andThen(nonNull);
  }

  @NotNull
  @Contract(pure = true)
  static <T> Function1<T, T> identity() {
    return t -> t;
  }
}
