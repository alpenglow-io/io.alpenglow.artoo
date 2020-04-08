package trydent.func.fun;

import trydent.func.Func;
import trydent.func.Functional;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> extends Functional.Fun {
  R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9);

  default <V> Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, V> then(Func<? super R, ? extends V> after) {
    requireNonNull(after);
    return (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9));
  }
}
