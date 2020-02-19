package oak.func;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Pre<T> extends Predicate<T>, Func<T, Boolean>, Functional.Pre {
  @Override
  default Boolean apply(T t) {
    return test(t);
  }

  @NotNull
  @Contract(pure = true)
  static <S> Pre<S> not(final Pre<S> predicate) {
    return it -> !requireNonNull(predicate, "Predicate is null").test(it);
  }

  @NotNull
  @Contract(pure = true)
  static <S> Pre<S> tautology() { return it -> true; }
}
