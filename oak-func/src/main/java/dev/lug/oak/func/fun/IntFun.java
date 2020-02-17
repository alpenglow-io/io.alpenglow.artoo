package dev.lug.oak.func.fun;

import dev.lug.oak.func.Fun;

import java.util.function.IntFunction;

@FunctionalInterface
public interface IntFun<R> extends Fun<Integer, R>, IntFunction<R> {
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
