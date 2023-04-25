package re.artoo.lance.func;

import java.util.function.Predicate;

@FunctionalInterface
public interface TryPredicate1<A> extends Predicate<A>, Invocable {
  static <A> TryPredicate1<A> not(final TryPredicate1<A> predicate) {
    return it -> !predicate.invoke(it);
  }

  boolean invoke(A a) throws Throwable;

  @Override
  default boolean test(A a) {
    return attempt(() -> invoke(a));
  }
}
