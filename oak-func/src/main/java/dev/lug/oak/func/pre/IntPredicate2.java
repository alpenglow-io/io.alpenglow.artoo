package dev.lug.oak.func.pre;

import dev.lug.oak.func.Functional;
import dev.lug.oak.func.fun.Function2;

@FunctionalInterface
public interface IntPredicate2<T> extends Predicate2<Integer, T>, Function2<Integer, T, Boolean>, Functional.Pre {
  boolean verify(final int index, final T param);

  @Override
  default Boolean apply(Integer index, T param) {
    return verify(index, param);
  }

  @Override
  default boolean test(Integer index, T param) {
    return verify(index, param);
  }
}
