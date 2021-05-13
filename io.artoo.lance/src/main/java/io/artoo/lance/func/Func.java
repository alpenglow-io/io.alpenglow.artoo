package io.artoo.lance.func;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface Func {
  interface Unary<T> extends UnaryOperator<T> {
    T tryApply(T t) throws Throwable;

    @Override
    default T apply(T t) {
      try {
        return tryApply(t);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
        return null;
      }
    }
  }

  interface Uni<T, R> extends Function<T, R> {
    R tryApply(T t) throws Throwable;

    @Override
    default R apply(T t) {
      try {
        return this.tryApply(t);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
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
        throwable.printStackTrace();
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
        throwable.printStackTrace();
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
        throwable.printStackTrace();
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
        throwable.printStackTrace();
        return null;
      }
    }
  }

  interface Sex<A, B, C, D, E, F, R> {
    R tryApply(A a, B b, C c, D d, E e, F f) throws Throwable;

    default R apply(A a, B b, C c, D d, E e, F f) {
      try {
        return this.tryApply(a, b, c, d, e, f);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
        return null;
      }
    }
  }

  interface Sect<A, B, C, D, E, F, G, R> {
    R tryApply(A a, B b, C c, D d, E e, F f, G g) throws Throwable;

    default R apply(A a, B b, C c, D d, E e, F f, G g) {
      try {
        return this.tryApply(a, b, c, d, e, f, g);
      } catch (Throwable throwable) {
        throwable.printStackTrace();
        return null;
      }
    }
  }

  enum Default {
    Nothing;

    public boolean notEquals(Object value) {
      return !this.equals(value);
    }
  }
  enum Leftover {__}
}
