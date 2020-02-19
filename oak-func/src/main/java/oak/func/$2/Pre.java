package oak.func.$2;

import oak.func.Functional;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface Pre<T1, T2> extends BiPredicate<T1, T2>, Func<T1, T2, Boolean>, Functional.Pre {
  @Override
  default Boolean apply(T1 t1, T2 t2) {
    return test(t1, t2);
  }
}
