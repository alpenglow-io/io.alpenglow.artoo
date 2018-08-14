package oak.func.pre;

import oak.func.Functional;
import oak.func.fun.Function2;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface Predicate2<T1, T2> extends BiPredicate<T1, T2>, Function2<T1, T2, Boolean>, Functional.Pre {
  @Override
  default Boolean apply(T1 t1, T2 t2) {
    return test(t1, t2);
  }
}
