package oak.func.fun;

import oak.func.Functional;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Function6<T1, T2, T3, T4, T5, T6, R> extends Functional.Fun {
  R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

  default <V> Function6<T1, T2, T3, T4, T5, T6, V> then(Function1<? super R, ? extends V> after) {
    requireNonNull(after);
    return (t1, t2, t3, t4, t5, t6) -> after.apply(apply(t1, t2, t3, t4, t5, t6));
  }
}