package io.alpenglow.artoo.lance.func;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface TryBiConsumer<A, B> extends BiConsumer<A, B> {
  void tryAccept(A a, B b) throws Throwable;

  @Override
  default void accept(A a, B b) {
    try {
      tryAccept(a, b);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
  }
}
