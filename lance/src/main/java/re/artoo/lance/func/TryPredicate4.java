package re.artoo.lance.func;

@FunctionalInterface
public interface TryPredicate4<A, B, C, D> {
  boolean invoke(A a, B b, C c, D d) throws Throwable;

  default boolean test(A a, B b, C c, D d) {
    try {
      return invoke(a, b, c, d);
    } catch (Throwable throwable) {
      throw new LambdaCallException(throwable);
    }
  }

  static <A, B, C, D> TryPredicate4<A, B, C, D> not(final TryPredicate4<A, B, C, D> predicate) {
    return (a, b, c, d) -> !predicate.invoke(a, b, c, d);
  }
}
