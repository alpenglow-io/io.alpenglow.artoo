package io.alpenglow.artoo.lance.func;

@FunctionalInterface
public interface TryPredicate3<A, B, C> {
  boolean invoke(A a, B b, C c) throws Throwable;
  default boolean test(A a, B b, C c) {
    try {
      return invoke(a, b, c);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }

  static <A, B, C> TryPredicate3<A, B, C> not(final TryPredicate3<A, B, C> predicate) {
    return (a, b, c) -> !predicate.invoke(a, b, c);
  }
}
