package lance.query.many;

import lance.func.Func.MaybeBiFunction;
import lance.func.Func.MaybeFunction;
import lance.func.tail.Select;
import lance.query.Many;
import lance.Queryable;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(MaybeBiFunction<? super Integer, ? super T, ? extends R> select) {
    return () -> cursor().map(rec(Select.with(select)));
  }

  default <R> Many<R> select(MaybeFunction<? super T, ? extends R> select) {
    return select((index, value) -> select.tryApply(value));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(MaybeBiFunction<? super Integer, ? super T, ? extends Q> select) {
    return () -> cursor().map(rec(Select.with(select))).flatMap(Queryable::cursor);
  }

  default <R, Q extends Queryable<R>> Many<R> selection(MaybeFunction<? super T, ? extends Q> select) {
    return selection((i, it) -> select.tryApply(it));
  }
}
