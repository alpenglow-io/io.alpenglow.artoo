package io.artoo.func;

import java.util.function.IntFunction;

@FunctionalInterface
public interface FuncInt<R> extends Func<Integer, R>, IntFunction<R> {
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
