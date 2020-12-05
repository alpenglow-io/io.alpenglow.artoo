package io.artoo.lance.func;

import io.artoo.lance.func.Func.Default;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static io.artoo.lance.func.Func.Default.Nothing;

public interface Cons {
  interface Uni<A> extends Consumer<A>, Func.Uni<A, Default> {
    static <T> Uni<T> nothing() {
      return it -> {};
    }

    void tryAccept(A a) throws Throwable;

    @Override
    default Default tryApply(A a) throws Throwable {
      tryAccept(a);
      return Nothing;
    }

    @Override
    default void accept(A a) {
      try {
        tryAccept(a);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }

    @Override
    default Default apply(A a) {
      accept(a);
      return Nothing;
    }
  }

  interface Bi<A, B> extends BiConsumer<A, B>, Func.Bi<A, B, Default> {
    void tryAccept(A a, B b) throws Throwable;

    @Override
    default Default tryApply(A a, B b) throws Throwable {
      tryAccept(a, b);
      return Nothing;
    }

    @Override
    default void accept(A a, B b) {
      try {
        tryAccept(a, b);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }

    @Override
    default Default apply(A a, B b) {
      accept(a, b);
      return Nothing;
    }
  }

  interface Tri<A, B, C> extends Func.Tri<A, B, C, Default> {
    void tryAccept(A a, B b, C c) throws Throwable;

    @Override
    default Default tryApply(A a, B b, C c) throws Throwable {
      tryAccept(a, b, c);
      return Nothing;
    }

    default void accept(A a, B b, C c) {
      try {
        tryAccept(a, b, c);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }

    @Override
    default Default apply(A a, B b, C c) {
      accept(a, b, c);
      return Nothing;
    }
  }

  interface Quad<A, B, C, D> extends Func.Quad<A, B, C, D, Default> {
    void tryAccept(A a, B b, C c, D d) throws Throwable;

    @Override
    default Default tryApply(A a, B b, C c, D d) throws Throwable {
      tryAccept(a, b, c, d);
      return Nothing;
    }

    default void accept(A a, B b, C c, D d) {
      try {
        tryAccept(a, b, c, d);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }

    @Override
    default Default apply(A a, B b, C c, D d) {
      accept(a, b, c, d);
      return Nothing;
    }
  }
}
