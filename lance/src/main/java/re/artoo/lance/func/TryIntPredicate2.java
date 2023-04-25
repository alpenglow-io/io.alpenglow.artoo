package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntPredicate2<A, B> extends Invocable {
  static <A, B> TryIntPredicate2<A, B> not(final TryIntPredicate2<A, B> predicate) {
    return (integer, it, es) -> !predicate.invoke(integer, it, es);
  }

  boolean invoke(int integer, A a, B b) throws Throwable;

  default boolean test(int integer, A a, B b) {
    return attempt(() -> invoke(integer, a, b));
  }
}
