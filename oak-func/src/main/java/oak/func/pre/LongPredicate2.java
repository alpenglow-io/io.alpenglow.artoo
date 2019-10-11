package oak.func.pre;

import oak.func.Functional;
import oak.func.fun.Function2;

@FunctionalInterface
public interface LongPredicate2<T> extends Predicate2<Long, T>, Function2<Long, T, Boolean>, Functional.Pre {
  boolean verify(final long index, final T param);

  @Override
  default Boolean apply(Long index, T param) {
    return verify(index, param);
  }

  @Override
  default boolean test(Long index, T param) {
    return verify(index, param);
  }
}
