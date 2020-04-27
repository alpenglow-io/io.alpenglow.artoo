package artoo.func.$3;

import artoo.func.Functional;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Cons<T1, T2, T3> extends Func<T1, T2, T3, Void>, Functional.Con {
  void accept(T1 t1, T2 t2, T3 t3);

  default Cons<T1, T2, T3> then(Cons<? super T1, ? super T2, ? super T3> after) {
    requireNonNull(after, "After-consumer can't ben null.");
    return (t1, t2, t3) -> {
      accept(t1, t2, t3);
      after.accept(t1, t2, t3);
    };
  }

  @Override
  default Void apply(T1 t1, T2 t2, T3 t3) {
    this.accept(t1, t2, t3);
    return null;
  }
}
