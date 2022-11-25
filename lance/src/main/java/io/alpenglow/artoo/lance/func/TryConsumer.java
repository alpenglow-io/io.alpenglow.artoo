package io.alpenglow.artoo.lance.func;

import java.util.function.Consumer;

@FunctionalInterface
public interface TryConsumer<A> extends Consumer<A> {
  static <T> TryConsumer<T> nothing() {
    return it -> {
    };
  }

  void tryAccept(A a) throws Throwable;

  @Override
  default void accept(A a) {
    try {
      tryAccept(a);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
  }
}
