package io.artoo.lance.func;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface Cons {
  interface Uni<A> extends Consumer<A>, Func.Uni<A, Void> {
    void consume(A a) throws Throwable;

    @Override
    default Void invoke(A a) throws Throwable {
      consume(a);
      return null;
    }

    @Override
    default void accept(A a) {
      try { consume(a); } catch (Throwable ignored) {}
    }

    @Override
    default Void apply(A a) {
      accept(a);
      return null;
    }
  }

  interface Bi<A, B> extends BiConsumer<A, B>, Func.Bi<A, B, Void> {
    void consume(A a, B b) throws Throwable;

    @Override
    default Void invoke(A a, B b) throws Throwable {
      consume(a, b);
      return null;
    }

    @Override
    default void accept(A a, B b) {
      try { consume(a, b); } catch (Throwable ignored) {}
    }

    @Override
    default Void apply(A a, B b) {
      accept(a, b);
      return null;
    }
  }

  interface Tri<A, B, C> extends Func.Tri<A, B, C, Void> {
    void consume(A a, B b, C c) throws Throwable;

    @Override
    default Void invoke(A a, B b, C c) throws Throwable {
      consume(a, b, c);
      return null;
    }

    default void accept(A a, B b, C c) {
      try { consume(a, b, c); } catch (Throwable ignored) {}
    }

    @Override
    default Void apply(A a, B b, C c) {
      accept(a, b, c);
      return null;
    }
  }

  interface Quad<A, B, C, D> extends Func.Quad<A, B, C, D, Void> {
    void consume(A a, B b, C c, D d) throws Throwable;

    @Override
    default Void invoke(A a, B b, C c, D d) throws Throwable {
      consume(a, b, c, d);
      return null;
    }

    default void accept(A a, B b, C c, D d) {
      try { consume(a, b, c, d); } catch (Throwable ignored) {}
    }

    @Override
    default Void apply(A a, B b, C c, D d) {
      accept(a, b, c, d);
      return null;
    }
  }
}
