package lance.query.many.oftwo;

import lance.func.Func;
import lance.func.tail.Select;
import lance.query.Many;
import lance.Queryable;
import lance.tuple.Pair;

public interface Projectable<A, B> extends Queryable.OfTwo<A, B> {
  default <R> Many<R> select(Func.MaybeTriFunction<? super Integer, ? super A, ? super B, ? extends R> select) {
    return () -> cursor().map(rec(Select.with((index, record) -> select.apply(index, record.first(), record.second()))));
  }

  default <R> Many<R> select(Func.MaybeBiFunction<? super A, ? super B, ? extends R> select) {
    return select((index, first, second) -> select.tryApply(first, second));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(Func.MaybeTriFunction<? super Integer, ? super A, ? super B, ? extends Q> select) {
    return () -> cursor().map(rec(Select.with(((i, pair) -> select.tryApply(i, pair.first(), pair.second()))))).flatMap(Queryable::cursor);
  }

  default <R, Q extends Queryable<R>> Many<R> selection(Func.MaybeBiFunction<? super A, ? super B, ? extends Q> select) {
    return selection((i, first, second) -> select.tryApply(first, second));
  }

  default <P extends Pair<A, B>> Many.OfTwo<A, B> to(Func.MaybeBiFunction<? super A, ? super B, ? extends P> func) {
    return () -> cursor().map(pair -> func.tryApply(pair.first(), pair.second()));
  }

  default <Q extends Queryable.OfTwo<A, B>> Many.OfTwo<A, B> too(Func.MaybeBiFunction<? super A, ? super B, ? extends Q> func) {
    return () -> cursor().flatMap(pair -> func.tryApply(pair.first(), pair.second()).cursor());
  }

}
