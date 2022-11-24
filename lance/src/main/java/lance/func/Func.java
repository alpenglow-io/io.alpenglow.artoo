package lance.func;

import lance.scope.Maybe;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public sealed interface Func {
  enum Namespace implements Func {}

  interface TryUnaryOperator<T> extends UnaryOperator<T> {
    T tryApply(T t) throws Throwable;

    default Maybe<T> maybeApply(T t) {
      try {
        return Maybe.value(tryApply(t));
      } catch (Throwable throwable) {
        return Maybe.error(throwable);
      }
    }

    @Override
    default T apply(T t) {
      return maybeApply(t).otherwise("Can't result unary operator", IllegalCallerException::new);
    }
  }

  interface TryFunction<T, R> extends Function<T, R> {
    R tryApply(T t) throws Throwable;

    default Maybe<R> maybeApply(T t) {
      try {
        return Maybe.value(this.tryApply(t));
      } catch (Throwable throwable) {
        return Maybe.error(throwable);
      }
    }

    @Override
    default R apply(T t) {
      return maybeApply(t).nullable();
    }

    default <V> TryFunction<V, R> previous(TryFunction<? super V, ? extends T> func) {
      return it -> {
        final var applied = func.tryApply(it);
        return applied == null ? null : tryApply(applied);
      };
    }

    default <V> TryFunction<T, V> then(TryFunction<? super R, ? extends V> func) {
      return it -> {
        final var applied = tryApply(it);
        return applied == null ? null : func.tryApply(applied);
      };
    }
  }

  interface TryBiFunction<A, B, R> extends BiFunction<A, B, R> {
    R tryApply(A a, B b) throws Throwable;

    default Maybe<R> maybeApply(A a, B b) {
      try {
        return Maybe.value(tryApply(a, b));
      } catch (Throwable throwable) {
        return Maybe.error(throwable);
      }
    }

    @Override
    default R apply(A a, B b) {
      return maybeApply(a, b).nullable();
    }

    default TryBiFunction<? super A, ? super B, ? extends R> butNulls() {
      return (a, b) -> a == null || b == null ? null : this.tryApply(a, b);
    }
  }

  interface TryTriFunction<A, B, C, R> {
    R tryApply(A a, B b, C c) throws Throwable;

    default Maybe<R> maybeApply(A a, B b, C c) {
      try {
        return Maybe.value(tryApply(a, b, c));
      } catch (Throwable throwable) {
        return Maybe.error(throwable);
      }
    }

    default R apply(A a, B b, C c) {
      return maybeApply(a, b, c).nullable();
    }
  }

  interface TryQuadFunction<A, B, C, D, R> {
    R tryApply(A a, B b, C c, D d) throws Throwable;

    default Maybe<R> maybeApply(A a, B b, C c, D d) {
      try {
        return Maybe.value(tryApply(a, b, c, d));
      } catch (Throwable throwable) {
        return Maybe.error(throwable);
      }
    }

    default R apply(A a, B b, C c, D d) {
      return maybeApply(a, b, c, d).nullable();
    }
  }

  interface TryQuinFunction<A, B, C, D, E, R> {
    R tryApply(A a, B b, C c, D d, E e) throws Throwable;

    default Maybe<R> apply(A a, B b, C c, D d, E e) {
      try {
        return Maybe.value(tryApply(a, b, c, d, e));
      } catch (Throwable throwable) {
        return Maybe.error(throwable);
      }
    }
  }

  interface TrySexFunction<A, B, C, D, E, F, R> {
    R tryApply(A a, B b, C c, D d, E e, F f) throws Throwable;

    default Maybe<R> apply(A a, B b, C c, D d, E e, F f) {
      try {
        return Maybe.value(tryApply(a, b, c, d, e, f));
      } catch (Throwable throwable) {
        return Maybe.error(throwable);
      }
    }
  }

  interface TrySectFunction<A, B, C, D, E, F, G, R> {
    R tryApply(A a, B b, C c, D d, E e, F f, G g) throws Throwable;

    default Maybe<R> apply(A a, B b, C c, D d, E e, F f, G g) {
      try {
        return Maybe.value(tryApply(a, b, c, d, e, f, g));
      } catch (Throwable throwable) {
        return Maybe.error(throwable);
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
