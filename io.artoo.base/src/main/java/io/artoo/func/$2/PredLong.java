package io.artoo.func.$2;

import io.artoo.func.Functional;

@FunctionalInterface
public interface PredLong<T> extends Pred<Long, T>, Functional.Pre {
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
