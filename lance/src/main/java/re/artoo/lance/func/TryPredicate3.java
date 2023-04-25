package re.artoo.lance.func;

@FunctionalInterface
public interface TryPredicate3<A, B, C> extends Invocable {
  static <A, B, C> TryPredicate3<A, B, C> not(final TryPredicate3<A, B, C> predicate) {
    return (a, b, c) -> !predicate.invoke(a, b, c);
  }

  boolean invoke(A a, B b, C c) throws Throwable;

  default boolean test(A a, B b, C c) {
    try {
      return attempt(() -> invoke(a, b, c));
    } catch (Throwable throwable) {
      throw throwable instanceof InvokeException it ? it : new InvokeException(throwable);
    }
  }
}
