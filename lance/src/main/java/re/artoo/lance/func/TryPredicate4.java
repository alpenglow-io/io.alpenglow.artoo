package re.artoo.lance.func;

@FunctionalInterface
public interface TryPredicate4<A, B, C, D> extends Invocable {
  static <A, B, C, D> TryPredicate4<A, B, C, D> not(final TryPredicate4<A, B, C, D> predicate) {
    return (a, b, c, d) -> !predicate.invoke(a, b, c, d);
  }

  boolean invoke(A a, B b, C c, D d) throws Throwable;

  default boolean test(A a, B b, C c, D d) {
    return attempt(() -> invoke(a, b, c, d));
  }
}
