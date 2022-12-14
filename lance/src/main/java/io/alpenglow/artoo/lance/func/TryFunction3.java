package io.alpenglow.artoo.lance.func;

@FunctionalInterface
public interface TryFunction3<A, B, C, R> {
  R invoke(A a, B b, C c) throws Throwable;
  default R apply(A a, B b, C c) {
    try {
      return invoke(a, b, c);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }
}
