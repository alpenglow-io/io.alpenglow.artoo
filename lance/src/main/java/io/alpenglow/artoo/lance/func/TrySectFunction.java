package io.alpenglow.artoo.lance.func;

import io.alpenglow.artoo.lance.scope.Maybe;

@FunctionalInterface
public interface TrySectFunction<A, B, C, D, E, F, G, R> {
  R tryApply(A a, B b, C c, D d, E e, F f, G g) throws Throwable;

  default Maybe<R> apply(A a, B b, C c, D d, E e, F f, G g) {
    try {
      return Maybe.value(tryApply(a, b, c, d, e, f, g));
    } catch (Throwable throwable) {
      return Maybe.error(throwable);
    }
  }
}
