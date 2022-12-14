package io.alpenglow.artoo.lance.func;

import java.util.function.Function;

@FunctionalInterface
public interface TryFunction1<T, R> extends Function<T, R> {
  R invoke(T t) throws Throwable;

  @Override
  default R apply(T t) {
    try {
      return invoke(t);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }

  default <V> TryFunction1<V, R> previous(TryFunction1<? super V, ? extends T> func) {
    return it -> {
      final var applied = func.invoke(it);
      return applied == null ? null : invoke(applied);
    };
  }

  default <V> TryFunction1<T, V> then(TryFunction1<? super R, ? extends V> func) {
    return it -> {
      final var applied = invoke(it);
      return applied == null ? null : func.invoke(applied);
    };
  }
}
