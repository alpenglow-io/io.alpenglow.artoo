package oak.func.$2;

import oak.func.Functional;

@FunctionalInterface
public interface LongPre<T> extends Pre<Long, T>, Func<Long, T, Boolean>, Functional.Pre {
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
