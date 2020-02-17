package dev.lug.oak.func;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Con<T> extends Consumer<T>, Functional.Con {
  default Con<T> then(Con<? super T> after) {
    requireNonNull(after);
    return t -> { accept(t); after.accept(t); };
  }
}
