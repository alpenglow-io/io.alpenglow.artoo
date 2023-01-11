package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntPredicate1<A> {
  boolean invoke(int integer, A a) throws Throwable;

  default boolean test(int integer, A a) {
    try {
      return invoke(integer, a);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }

  static <A> TryIntPredicate1<A> not(final TryIntPredicate1<A> predicate) {
    return (integer, it) -> !predicate.invoke(integer, it);
  }
}
