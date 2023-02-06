package re.artoo.lance.func;

@FunctionalInterface
public interface TryBooleanPredicate2<A, B> {
  boolean invoke(boolean truth, A a, B b) throws Throwable;

  default boolean test(boolean truth, A a, B b) {
    try {
      return invoke(truth, a, b);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }

  static <A, B> TryBooleanPredicate2<A, B> not(final TryBooleanPredicate2<A, B> predicate) {
    return (truth, a, b) -> !predicate.invoke(truth, a, b);
  }
}
