package io.alpenglow.artoo.lance.func;

@FunctionalInterface
public interface TryQuadPredicate<A, B, C, D> extends TryQuadFunction<A, B, C, D, Boolean> {
  boolean tryTest(A a, B b, C c, D d) throws Throwable;

  @Override
  default Boolean tryApply(A a, B b, C c, D d) throws Throwable {
    return tryTest(a, b, c, d);
  }

  static <A, B, C, D> TryQuadPredicate<A, B, C, D> not(final TryQuadPredicate<A, B, C, D> predicate) {
    return (a, b, c, d) -> !predicate.tryTest(a, b, c, d);
  }
}
