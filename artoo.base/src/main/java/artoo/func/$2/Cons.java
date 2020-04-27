package artoo.func.$2;

import artoo.func.Functional;

import java.util.function.BiConsumer;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Cons<T1, T2> extends BiConsumer<T1, T2>, Func<T1, T2, Void>, Functional.Con {
  default Cons<T1, T2> then(Cons<? super T1, ? super T2> after) {
    requireNonNull(after);
    return (t1, t2) -> {
      accept(t1, t2);
      after.accept(t1, t2);
    };
  }

  @Override
  default Void apply(T1 t1, T2 t2) {
    this.accept(t1, t2);
    return null;
  }
}
