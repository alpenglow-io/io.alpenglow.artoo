package io.alpenglow.artoo.lance.func;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface TryBiPredicate<A, B> extends BiPredicate<A, B>, TryBiFunction<A, B, Boolean> {
  boolean tryTest(A a, B b) throws Throwable;

  @Override
  default Boolean tryApply(A a, B b) throws Throwable {
    return tryTest(a, b);
  }

  @Override
  default boolean test(A a, B b) {
    try {
      return tryTest(a, b);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return false;
    }
  }

  static <A, B> TryBiPredicate<A, B> not(final TryBiPredicate<A, B> predicate) {
    return (a, b) -> !predicate.tryTest(a, b);
  }
}
