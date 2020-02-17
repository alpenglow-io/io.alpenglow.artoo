package dev.lug.oak.func.$2;

import dev.lug.oak.func.Functional;

import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

public interface Fun<T1, T2, R> extends BiFunction<T1, T2, R>, Functional.Fun {
  default <V> Fun<T1, T2, V> then(dev.lug.oak.func.Fun<? super R, ? extends V> after) {
    requireNonNull(after);
    return (T1 t1, T2 t2) -> after.apply(apply(t1, t2));
  }
}
