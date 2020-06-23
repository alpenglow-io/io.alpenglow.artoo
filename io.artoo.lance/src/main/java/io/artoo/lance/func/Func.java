package io.artoo.lance.func;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Func {
  interface Uni<T, R> extends Function<T, R> {
    R invoke(T t) throws Throwable;

    @Override
    default R apply(T t) {
      try {
        return this.invoke(t);
      } catch (Throwable throwable) {
        return null;
      }
    }
  }

  interface Bi<A, B, R> extends BiFunction<A, B, R> {
    R invoke(A a, B b) throws Throwable;

    @Override
    default R apply(A a, B b) {
      try {
        return this.invoke(a, b);
      } catch (Throwable throwable) {
        return null;
      }
    }
  }

  interface Tri<A, B, C, R> {
    R invoke(A a, B b, C c) throws Throwable;

    default R apply(A a, B b, C c) {
      try {
        return this.invoke(a, b, c);
      } catch (Throwable throwable) {
        return null;
      }
    }
  }

  interface Quad<A, B, C, D, R> {
    R invoke(A a, B b, C c, D d) throws Throwable;

    default R apply(A a, B b, C c, D d) {
      try {
        return this.invoke(a, b, c, d);
      } catch (Throwable throwable) {
        return null;
      }
    }
  }

}
