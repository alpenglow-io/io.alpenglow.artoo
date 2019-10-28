package dev.lug.oak.func.con;

import dev.lug.oak.func.Functional;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Consumer1<T> extends Consumer<T>, Functional.Con {
  default Consumer1<T> then(Consumer1<? super T> after) {
    requireNonNull(after);
    return t -> { accept(t); after.accept(t); };
  }
}
