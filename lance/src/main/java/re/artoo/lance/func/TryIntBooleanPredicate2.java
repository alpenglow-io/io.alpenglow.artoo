package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntBooleanPredicate2<A, B> {
  boolean invoke(int value, boolean truth, A a, B b) throws Throwable;

  default boolean test(int value, boolean truth, A a, B b) {
    try {
      return invoke(value, truth, a, b);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }

  static <A, B> TryIntBooleanPredicate2<A, B> not(final TryIntBooleanPredicate2<A, B> predicate) {
    return (value, truth, a, b) -> !predicate.invoke(value, truth, a, b);
  }
}
