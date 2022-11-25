package io.alpenglow.artoo.lance.func;

import io.alpenglow.artoo.lance.scope.Maybe;

import java.util.function.Predicate;

@FunctionalInterface
public interface TryPredicate<A> extends Predicate<A> {
  boolean tryTest(A a) throws Throwable;

  default Maybe<Boolean> verify(A a) {
    try {
      return Maybe.value(tryTest(a));
    } catch (Throwable throwable) {
      return Maybe.error(throwable);
    }
  }

  @Override
  default boolean test(A a) {
    return verify(a).or(() -> false).nullable();
  }

  default TryPredicate<A> not() {
    return it -> !this.tryTest(it);
  }

  static <A> TryPredicate<A> not(final TryPredicate<A> predicate) {
    return it -> !predicate.tryTest(it);
  }
}
