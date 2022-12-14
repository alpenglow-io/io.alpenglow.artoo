package io.alpenglow.artoo.lance.func;

@FunctionalInterface
public interface TryConsumer3<A, B, C> {
  void invoke(A a, B b, C c) throws Throwable;
  default void accept(A a, B b, C c) {
    try {
      invoke(a, b, c);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }
}
