package dev.lug.oak.func.fun;

import java.util.function.IntFunction;

@FunctionalInterface
public interface IntFunction1<R> extends Function1<Integer, R>, IntFunction<R> {
  R applyInt(final int value);

  @Override
  default R apply(final Integer value) {
    return applyInt(value);
  }

  @Override
  default R apply(final int value) {
    return applyInt(value);
  }
}
