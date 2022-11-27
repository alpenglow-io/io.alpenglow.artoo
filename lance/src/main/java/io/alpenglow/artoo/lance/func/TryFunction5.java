package io.alpenglow.artoo.lance.func;

@FunctionalInterface
public interface TryFunction5<A, B, C, D, E, R> {
  R tryApply(A a, B b, C c, D d, E e) throws Throwable;

  default R apply(A a, B b, C c, D d, E e) {
    try {
      return tryApply(a, b, c, d, e);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }
}
