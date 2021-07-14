package io.artoo.lance.func;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public enum Cons {;
  public interface Uni<A> extends Consumer<A>, Func.Uni<A, Void> {
    static <T> Uni<T> nothing() {
      return it -> {};
    }

    void tryAccept(A a) throws Throwable;

    @Override
    default Void tryApply(A a) throws Throwable {
      tryAccept(a);
      return null;
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
    default Void apply(A a) {
      accept(a);
      return null;
    }
  }

  public interface Bi<A, B> extends BiConsumer<A, B>, Func.Bi<A, B, Void> {
    void tryAccept(A a, B b) throws Throwable;

    @Override
    default Void tryApply(A a, B b) throws Throwable {
      tryAccept(a, b);
      return null;
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
    default Void apply(A a, B b) {
      accept(a, b);
      return null;
    }
  }

  public interface Tri<A, B, C> extends Func.Tri<A, B, C, Void> {
    void tryAccept(A a, B b, C c) throws Throwable;

    @Override
    default Void tryApply(A a, B b, C c) throws Throwable {
      tryAccept(a, b, c);
      return null;
    }

    default void accept(A a, B b, C c) {
      try {
        tryAccept(a, b, c);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }

    @Override
    default Void apply(A a, B b, C c) {
      accept(a, b, c);
      return null;
    }
  }

  public interface Quad<A, B, C, D> extends Func.Quad<A, B, C, D, Void> {
    void tryAccept(A a, B b, C c, D d) throws Throwable;

    @Override
    default Void tryApply(A a, B b, C c, D d) throws Throwable {
      tryAccept(a, b, c, d);
      return null;
    }

    default void accept(A a, B b, C c, D d) {
      try {
        tryAccept(a, b, c, d);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }

    @Override
    default Void apply(A a, B b, C c, D d) {
      accept(a, b, c, d);
      return null;
    }
  }

  public interface Quin<A, B, C, D, E> extends Func.Quin<A, B, C, D, E, Void> {
    void tryAccept(A a, B b, C c, D d, E e) throws Throwable;

    @Override
    default Void tryApply(A a, B b, C c, D d, E e) throws Throwable {
      tryAccept(a, b, c, d, e);
      return null;
    }

    default void accept(A a, B b, C c, D d, E e) {
      try {
        tryAccept(a, b, c, d, e);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }

    @Override
    default Void apply(A a, B b, C c, D d, E e) {
      accept(a, b, c, d, e);
      return null;
    }
  }
}
