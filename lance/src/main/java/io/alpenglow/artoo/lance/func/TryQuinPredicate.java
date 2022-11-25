package io.alpenglow.artoo.lance.func;

@FunctionalInterface
public interface TryQuinPredicate<A, B, C, D, E> extends TryQuinFunction<A, B, C, D, E, Boolean> {
  boolean tryTest(A a, B b, C c, D d, E e) throws Throwable;

  @Override
  default Boolean tryApply(A a, B b, C c, D d, E e) throws Throwable {
    return tryTest(a, b, c, d, e);
  }

  static <A, B, C, D, E> TryQuinPredicate<A, B, C, D, E> not(final TryQuinPredicate<A, B, C, D, E> predicate) {
    return (a, b, c, d, e) -> !predicate.tryTest(a, b, c, d, e);
  }
}
