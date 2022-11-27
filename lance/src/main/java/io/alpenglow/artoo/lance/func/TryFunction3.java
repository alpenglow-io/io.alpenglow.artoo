package io.alpenglow.artoo.lance.func;

import io.alpenglow.artoo.lance.scope.Maybe;

@FunctionalInterface
public interface TryFunction3<A, B, C, R> {
  R tryApply(A a, B b, C c) throws Throwable;
  default R apply(A a, B b, C c) {
    Maybe<R> result;
    try {
      return tryApply(a, b, c);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }
}
