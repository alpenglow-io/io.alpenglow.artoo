package io.alpenglow.artoo.lance.func;

@FunctionalInterface
public interface TryTriPredicate<A, B, C> extends TryTriFunction<A, B, C, Boolean> {
  boolean tryTest(A a, B b, C c) throws Throwable;

  @Override
  default Boolean tryApply(A a, B b, C c) throws Throwable {
    return tryTest(a, b, c);
  }

  static <A, B, C> TryTriPredicate<A, B, C> not(final TryTriPredicate<A, B, C> predicate) {
    return (a, b, c) -> !predicate.tryTest(a, b, c);
  }
}
