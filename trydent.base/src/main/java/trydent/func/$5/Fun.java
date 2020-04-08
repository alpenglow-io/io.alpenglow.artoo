package trydent.func.$5;

import trydent.func.Func;
import trydent.func.Functional;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Fun<T1, T2, T3, T4, T5, R> extends Functional.Fun {
  R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);

  default <V> Fun<T1, T2, T3, T4, T5, V> then(Func<? super R, ? extends V> after) {
    requireNonNull(after);
    return (t1, t2, t3, t4, t5) -> after.apply(apply(t1, t2, t3, t4, t5));
  }
}
