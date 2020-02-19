package oak.func.$4;

import oak.func.Func;
import oak.func.Functional;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Fun<T1, T2, T3, T4, R> extends Functional.Fun {
  R apply(T1 t1, T2 t2, T3 t3, T4 t4);

  default <V> Fun<T1, T2, T3, T4, V> then(Func<? super R, ? extends V> after) {
    requireNonNull(after);
    return (t1, t2, t3, t4) -> after.apply(apply(t1, t2, t3, t4));
  }
}
