package oak.func.fun;

import oak.func.Functional;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Function1<T, R> extends Function<T, R>, Functional.Fun {
  default <V> Function<V, R> before(Function<? super V, ? extends T> before) {
    return compose(requireNonNull(before, "Before is null"));
  }

  default <V> Function<T, V> after(Function<? super R, ? extends V> after) {
    return andThen(requireNonNull(after, "After is null"));
  }

  static <T> Function1<T, T> identity() {
    return t -> t;
  }
}
