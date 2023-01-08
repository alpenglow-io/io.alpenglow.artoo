package re.artoo.lance.func;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface TryPredicate2<A, B> extends BiPredicate<A, B> {
  boolean invoke(A a, B b) throws Throwable;
  @Override
  default boolean test(A a, B b) {
    try {
      return invoke(a, b);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }
  static <A, B> TryPredicate2<A, B> not(final TryPredicate2<A, B> predicate) {
    return (a, b) -> !predicate.invoke(a, b);
  }
}
