package io.alpenglow.artoo.lance.func;

@FunctionalInterface
public interface TryFunction4<A, B, C, D, R> {
  R tryApply(A a, B b, C c, D d) throws Throwable;

  default R apply(A a, B b, C c, D d) {
    try {
      return tryApply(a, b, c, d);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }
}
