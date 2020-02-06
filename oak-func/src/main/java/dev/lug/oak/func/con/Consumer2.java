package dev.lug.oak.func.con;

import dev.lug.oak.func.Functional;
import dev.lug.oak.func.fun.Function2;

import java.util.function.BiConsumer;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Consumer2<T1, T2> extends BiConsumer<T1, T2>, Function2<T1, T2, Void>, Functional.Con {
  default Consumer2<T1, T2> then(Consumer2<? super T1, ? super T2> after) {
    requireNonNull(after);
    return (t1, t2) -> { accept(t1, t2); after.accept(t1, t2); };
  }

  @Override
  default Void apply(T1 t1, T2 t2) {
    this.accept(t1, t2);
    return null;
  }
}
