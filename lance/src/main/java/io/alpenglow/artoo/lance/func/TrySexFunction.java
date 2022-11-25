package io.alpenglow.artoo.lance.func;

import io.alpenglow.artoo.lance.scope.Maybe;

@FunctionalInterface
public interface TrySexFunction<A, B, C, D, E, F, R> {
  R tryApply(A a, B b, C c, D d, E e, F f) throws Throwable;

  default Maybe<R> apply(A a, B b, C c, D d, E e, F f) {
    try {
      return Maybe.value(tryApply(a, b, c, d, e, f));
    } catch (Throwable throwable) {
      return Maybe.error(throwable);
    }
  }
}
