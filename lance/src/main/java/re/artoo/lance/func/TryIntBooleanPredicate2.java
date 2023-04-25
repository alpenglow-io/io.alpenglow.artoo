package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntBooleanPredicate2<A, B> extends Invocable {
  static <A, B> TryIntBooleanPredicate2<A, B> not(final TryIntBooleanPredicate2<A, B> predicate) {
    return (value, truth, a, b) -> !predicate.invoke(value, truth, a, b);
  }

  boolean invoke(int value, boolean truth, A a, B b) throws Throwable;

  default boolean test(int value, boolean truth, A a, B b) {
    return attempt(() -> invoke(value, truth, a, b));
  }
}
