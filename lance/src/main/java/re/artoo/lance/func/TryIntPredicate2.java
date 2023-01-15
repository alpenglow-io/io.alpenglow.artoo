package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntPredicate2<A, B> {
  boolean invoke(int integer, A a, B b) throws Throwable;

  default boolean test(int integer, A a, B b) {
    try {
      return invoke(integer, a, b);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }

  static <A, B> TryIntPredicate2<A, B> not(final TryIntPredicate2<A, B> predicate) {
    return (integer, it, es) -> !predicate.invoke(integer, it, es);
  }
}
