package dev.lug.oak.func.pre;

import dev.lug.oak.func.Functional;
import dev.lug.oak.func.fun.Function1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Predicate1<T> extends Predicate<T>, Function1<T, Boolean>, Functional.Pre {
  @Override
  default Boolean apply(T t) {
    return test(t);
  }

  @NotNull
  @Contract(pure = true)
  static <S> Predicate1<S> not(final Predicate1<S> predicate) {
    return it -> !requireNonNull(predicate, "Predicate is null").test(it);
  }

  static <S> Predicate1<S> tautology() { return it -> true; }
}
