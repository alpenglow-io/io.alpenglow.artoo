package io.alpenglow.artoo.lance.func;

import io.alpenglow.artoo.lance.scope.Maybe;

@FunctionalInterface
public interface TryQuinFunction<A, B, C, D, E, R> {
  R tryApply(A a, B b, C c, D d, E e) throws Throwable;

  default Maybe<R> apply(A a, B b, C c, D d, E e) {
    try {
      return Maybe.value(tryApply(a, b, c, d, e));
    } catch (Throwable throwable) {
      return Maybe.error(throwable);
    }
  }
}
