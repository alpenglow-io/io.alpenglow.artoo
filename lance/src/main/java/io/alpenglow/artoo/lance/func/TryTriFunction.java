package io.alpenglow.artoo.lance.func;

import io.alpenglow.artoo.lance.scope.Maybe;

@FunctionalInterface
public interface TryTriFunction<A, B, C, R> {
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
