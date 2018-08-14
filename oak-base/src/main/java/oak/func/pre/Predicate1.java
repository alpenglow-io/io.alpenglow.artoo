package oak.func.pre;

import oak.func.Functional;
import oak.func.fun.Function1;

import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Predicate1<T> extends Predicate<T>, Function1<T, Boolean>, Functional.Pre {
  @Override
  default Boolean apply(T t) {
    return test(t);
  }

  static <S> Predicate1<S> not(final Predicate1<S> predicate) {
    return it -> !requireNonNull(predicate, "Predicate is null").test(it);
  }
}
