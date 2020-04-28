package io.artoo.func;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static io.artoo.type.Nullability.*;

@FunctionalInterface
public interface Func<T, R> extends Function<T, R>, Functional.Fun {
  @NotNull
  @Contract(pure = true)
  static <T> Func<T, T> identity() {
    return t -> t;
  }

  default <V> Function<V, R> before(Function<? super V, ? extends T> before) {
    return compose(nonNullable(before, "Before is null"));
  }

  default <V> Function<T, V> after(Function<? super R, ? extends V> after) {
    return andThen(nonNullable(after, "after"));
  }
}
