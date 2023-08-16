package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntPredicate1<A> extends Invocable {
  static <A> TryIntPredicate1<A> not(final TryIntPredicate1<A> predicate) {
    return (integer, it) -> !predicate.invoke(integer, it);
  }

  boolean invoke(int value, A a) throws Throwable;

  default TryIntPredicate1<A> oppose() {
    return (number, element) -> !invoke(number, element);
  }

  default boolean test(int integer, A a) {
    return attempt(() -> invoke(integer, a));
  }
}
