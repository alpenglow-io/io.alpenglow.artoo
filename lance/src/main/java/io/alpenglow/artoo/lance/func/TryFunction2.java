package io.alpenglow.artoo.lance.func;

import java.util.function.BiFunction;

@FunctionalInterface
public interface TryFunction2<A, B, R> extends BiFunction<A, B, R> {
  R tryApply(A a, B b) throws Throwable;

  @Override
  default R apply(A a, B b) {
    try {
      return tryApply(a, b);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }
}
