package io.alpenglow.artoo.lance.func;

@FunctionalInterface
public interface TryConsumer5<A, B, C, D, E> {
  void tryAccept(A a, B b, C c, D d, E e) throws Throwable;

  default void accept(A a, B b, C c, D d, E e) {
    try {
      tryAccept(a, b, c, d, e);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }
}
