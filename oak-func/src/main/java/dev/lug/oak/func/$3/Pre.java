package dev.lug.oak.func.$3;

import dev.lug.oak.func.Functional;

import java.util.Objects;

@FunctionalInterface
public interface Pre<T1, T2, T3> extends Fun<T1, T2, T3, Boolean>, Functional.Pre {
  boolean test(T1 t1, T2 t2, T3 t3);

  @Override
  default Boolean apply(T1 t, T2 t2, T3 t3) {
    return test(t, t2, t3);
  }

  default Pre<T1, T2, T3> and(Pre<? super T1, ? super T2, ? super T3> other) {
    Objects.requireNonNull(other);
    return (T1 t1, T2 t2, T3 t3) -> test(t1, t2, t3) && other.test(t1, t2, t3);
  }

  default Pre<T1, T2, T3> negate() {
    return (T1 t1, T2 t2, T3 t3) -> !test(t1, t2, t3);
  }

  default Pre<T1, T2, T3> or(Pre<? super T1, ? super T2, ? super T3> other) {
    Objects.requireNonNull(other);
    return (T1 t1, T2 t2, T3 t3) -> test(t1, t2, t3) || other.test(t1, t2, t3);
  }
}
