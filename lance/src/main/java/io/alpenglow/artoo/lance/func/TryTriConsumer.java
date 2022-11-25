package io.alpenglow.artoo.lance.func;

@FunctionalInterface
public interface TryTriConsumer<A, B, C> {
  void tryAccept(A a, B b, C c) throws Throwable;

  default void accept(A a, B b, C c) {
    try {
      tryAccept(a, b, c);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
  }
}
