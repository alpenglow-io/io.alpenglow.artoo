package lance.query.many;

import lance.func.Func;
import lance.query.One;
import lance.Queryable;
import lance.query.func.Extreme;

public interface Extremable<T> extends Queryable<T> {
  private One<T> extreme() {
    return this.extreme(it -> it instanceof Number n ? n : null);
  }

  private <N extends Number, V> One<V> extreme(final Func.TryFunction<? super T, ? extends N> select) {
    return () -> cursor().map(rec(Extreme.<T, N, V>max(select))).keepNull();
  }

  default <N extends Number> One<N> max(final Func.TryFunction<? super T, ? extends N> select) {
    return () -> cursor().map(rec(Extreme.<T, N, N>max(select))).keepNull();
  }

  default One<T> max() {
    return () -> cursor().map(rec(Extreme.<T, Number, T>max())).keepNull();
  }

  default <N extends Number> One<N> min(final Func.TryFunction<? super T, ? extends N> select) {
    return () -> cursor().map(rec(Extreme.<T, N, N>min(select))).keepNull();
  }

  default One<T> min() {
    return () -> cursor().map(rec(Extreme.<T, Number, T>min())).keepNull();
  }

}

