package oak.func.$3;

import oak.func.Functional;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Func<T1, T2, T3, R> extends Functional.Fun {
  R apply(T1 t, T2 t2, T3 t3);

  default <V> Func<T1, T2, T3, V> then(oak.func.Func<? super R, ? extends V> after) {
    requireNonNull(after);
    return (t1, t2, t3) -> after.apply(apply(t1, t2, t3));
  }
}
