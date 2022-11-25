package io.alpenglow.artoo.lance.func;

import io.alpenglow.artoo.lance.scope.Maybe;

@FunctionalInterface
public interface TryQuadFunction<A, B, C, D, R> {
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
