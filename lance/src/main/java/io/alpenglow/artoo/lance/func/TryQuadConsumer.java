package io.alpenglow.artoo.lance.func;

@FunctionalInterface
public interface TryQuadConsumer<A, B, C, D> {
  void tryAccept(A a, B b, C c, D d) throws Throwable;

  default void accept(A a, B b, C c, D d) {
    try {
      tryAccept(a, b, c, d);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
  }
}
