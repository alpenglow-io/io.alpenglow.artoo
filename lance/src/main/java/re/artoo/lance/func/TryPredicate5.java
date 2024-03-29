package re.artoo.lance.func;

@FunctionalInterface
public interface TryPredicate5<A, B, C, D, E> extends Invocable {
  static <A, B, C, D, E> TryPredicate5<A, B, C, D, E> not(final TryPredicate5<A, B, C, D, E> predicate) {
    return (a, b, c, d, e) -> !predicate.invoke(a, b, c, d, e);
  }

  boolean invoke(A a, B b, C c, D d, E e) throws Throwable;

  default boolean test(A a, B b, C c, D d, E e) {
    return attempt(() -> invoke(a, b, c, d, e));
  }
}
