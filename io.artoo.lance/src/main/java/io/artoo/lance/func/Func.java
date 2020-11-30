package io.artoo.lance.func;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Func {
  interface Uni<T, R> extends Function<T, R> {
    R tryApply(T t) throws Throwable;

    @Override
    default R apply(T t) {
      try {
        return this.tryApply(t);
      } catch (Throwable throwable) {
        return null;
      }
    }

    default <V> Func.Uni<V, R> previous(Func.Uni<? super V, ? extends T> func) {
      assert func != null;
      return it -> {
        final var applied = func.tryApply(it);
        return applied == null ? null : tryApply(applied);
      };
    }

    default <V> Func.Uni<T, V> then(Func.Uni<? super R, ? extends V> func) {
      assert func != null;
      return it -> {
        final var applied = tryApply(it);
        return applied == null ? null : func.tryApply(applied);
      };
    }
  }

  interface Bi<A, B, R> extends BiFunction<A, B, R> {
    R tryApply(A a, B b) throws Throwable;

    @Override
    default R apply(A a, B b) {
      try {
        return this.tryApply(a, b);
      } catch (Throwable throwable) {
        return null;
      }
    }

    default Func.Bi<? super A, ? super B, ? extends R> butNulls() {
      return (a, b) -> a == null || b == null ? null : this.tryApply(a, b);
    }
  }

  interface Tri<A, B, C, R> {
    R tryApply(A a, B b, C c) throws Throwable;

    default R apply(A a, B b, C c) {
      try {
        return this.tryApply(a, b, c);
      } catch (Throwable throwable) {
        return null;
      }
    }
  }

  interface Quad<A, B, C, D, R> {
    R tryApply(A a, B b, C c, D d) throws Throwable;

    default R apply(A a, B b, C c, D d) {
      try {
        return this.tryApply(a, b, c, d);
      } catch (Throwable throwable) {
        return null;
      }
    }
  }

  interface Quin<A, B, C, D, E, R> {
    R tryApply(A a, B b, C c, D d, E e) throws Throwable;

    default R apply(A a, B b, C c, D d, E e) {
      try {
        return this.tryApply(a, b, c, d, e);
      } catch (Throwable throwable) {
        return null;
      }
    }
  }

  enum Default {Nothing}
  enum Leftover {__}
}
