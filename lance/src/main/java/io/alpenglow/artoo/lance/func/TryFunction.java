package io.alpenglow.artoo.lance.func;

import io.alpenglow.artoo.lance.scope.Maybe;

import java.util.function.Function;

@FunctionalInterface
public interface TryFunction<T, R> extends Function<T, R> {
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
