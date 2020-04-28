package io.artoo.func;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Cons<T> extends Consumer<T>, Func<T, Void>, Functional.Con {
  @NotNull
  @Contract(pure = true)
  static <T> Cons<T> nothing() {
    return it -> {};
  }

  @Override
  default Void apply(T it) {
    accept(it);
    return null;
  }

  default Cons<T> then(Cons<? super T> after) {
    requireNonNull(after);
    return t -> {
      accept(t);
      after.accept(t);
    };
  }
}
