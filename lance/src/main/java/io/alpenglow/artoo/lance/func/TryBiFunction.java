package io.alpenglow.artoo.lance.func;

import io.alpenglow.artoo.lance.scope.Maybe;

import java.util.function.BiFunction;

@FunctionalInterface
public interface TryBiFunction<A, B, R> extends BiFunction<A, B, R> {
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
