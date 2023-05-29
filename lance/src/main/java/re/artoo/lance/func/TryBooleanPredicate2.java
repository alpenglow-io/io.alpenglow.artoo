package re.artoo.lance.func;

@FunctionalInterface
public interface TryBooleanPredicate2<A, B> extends Invocable {
  static <A, B> TryBooleanPredicate2<A, B> not(final TryBooleanPredicate2<A, B> predicate) {
    return (truth, a, b) -> !predicate.invoke(truth, a, b);
  }

  boolean invoke(boolean truth, A a, B b) throws Throwable;

  default boolean test(boolean truth, A a, B b) {
    return attempt(() -> invoke(truth, a, b));
  }
}
