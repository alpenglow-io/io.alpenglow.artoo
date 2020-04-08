package trydent.func.$2;

import trydent.func.Functional;

import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

public interface Func<T1, T2, R> extends BiFunction<T1, T2, R>, Functional.Fun {
  default <V> Func<T1, T2, V> then(trydent.func.Func<? super R, ? extends V> after) {
    requireNonNull(after);
    return (T1 t1, T2 t2) -> after.apply(apply(t1, t2));
  }
}
