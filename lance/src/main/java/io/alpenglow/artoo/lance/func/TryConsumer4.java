package io.alpenglow.artoo.lance.func;

@FunctionalInterface
public interface TryConsumer4<A, B, C, D> {
  void tryAccept(A a, B b, C c, D d) throws Throwable;

  default void accept(A a, B b, C c, D d) {
    try {
      tryAccept(a, b, c, d);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }
}
