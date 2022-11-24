package lance.query.many;

import lance.func.Func.TryBiFunction;
import lance.func.Func.TryFunction;
import lance.func.tail.Select;
import lance.query.Many;
import lance.Queryable;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(TryBiFunction<? super Integer, ? super T, ? extends R> select) {
    return () -> cursor().map(rec(Select.with(select)));
  }

  default <R> Many<R> select(TryFunction<? super T, ? extends R> select) {
    return select((index, value) -> select.tryApply(value));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(TryBiFunction<? super Integer, ? super T, ? extends Q> select) {
    return () -> cursor().map(rec(Select.with(select))).flatMap(Queryable::cursor);
  }

  default <R, Q extends Queryable<R>> Many<R> selection(TryFunction<? super T, ? extends Q> select) {
    return selection((i, it) -> select.tryApply(it));
  }
}
