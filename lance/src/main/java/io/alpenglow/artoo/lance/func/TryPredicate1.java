package io.alpenglow.artoo.lance.func;

import java.util.function.Predicate;

@FunctionalInterface
public interface TryPredicate1<A> extends Predicate<A> {
  boolean tryTest(A a) throws Throwable;

  @Override
  default boolean test(A a) {
    try {
      return tryTest(a);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }

  static <A> TryPredicate1<A> not(final TryPredicate1<A> predicate) {
    return it -> !predicate.tryTest(it);
  }
}
