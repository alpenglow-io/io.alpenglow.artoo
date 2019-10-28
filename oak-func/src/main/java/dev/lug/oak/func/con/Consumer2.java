package dev.lug.oak.func.con;

import dev.lug.oak.func.Functional;

import java.util.function.BiConsumer;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Consumer2<T1, T2> extends BiConsumer<T1, T2>, Functional.Con {
  default Consumer2<T1, T2> then(Consumer2<? super T1, ? super T2> after) {
    requireNonNull(after);
    return (t1, t2) -> { accept(t1, t2); after.accept(t1, t2); };
  }
}
