package lance.func;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public sealed interface Cons {
  enum Namespace implements Cons {}

  interface TryConsumer<A> extends Consumer<A> {
    static <T> TryConsumer<T> nothing() {
      return it -> {};
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

  interface TryBiConsumer<A, B> extends BiConsumer<A, B> {
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

  interface TryTriConsumer<A, B, C> {
    void tryAccept(A a, B b, C c) throws Throwable;

    default void accept(A a, B b, C c) {
      try {
        tryAccept(a, b, c);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }
  }

  interface TryQuadConsumer<A, B, C, D> {
    void tryAccept(A a, B b, C c, D d) throws Throwable;

    default void accept(A a, B b, C c, D d) {
      try {
        tryAccept(a, b, c, d);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }
  }

  interface TryQuinConsumer<A, B, C, D, E> {
    void tryAccept(A a, B b, C c, D d, E e) throws Throwable;

    default void accept(A a, B b, C c, D d, E e) {
      try {
        tryAccept(a, b, c, d, e);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }
  }
}
