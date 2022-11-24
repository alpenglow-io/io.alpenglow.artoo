package lance.query.one.oftwo;

import lance.func.Func;
import lance.func.Func.MaybeTriFunction;
import lance.func.tail.Select;
import lance.query.One;
import lance.Queryable;
import lance.tuple.Pair;

public interface Projectable<A, B> extends Queryable.OfTwo<A, B> {
  default <R> One<R> select(MaybeTriFunction<? super Integer, ? super A, ? super B, ? extends R> select) {
    return () -> cursor().map(rec(Select.with((index, record) -> select.apply(index, record.first(), record.second()))));
  }

  default <R> One<R> select(Func.MaybeBiFunction<? super A, ? super B, ? extends R> select) {
    return select((index, first, second) -> select.tryApply(first, second));
  }

  default <R, O extends One<R>> One<R> selection(MaybeTriFunction<? super Integer, ? super A, ? super B, ? extends O> select) {
    return () -> cursor().map(rec(Select.with((i, pair) -> select.tryApply(i, pair.first(), pair.second())))).flatMap(Queryable::cursor);
  }

  default <R, O extends One<R>> One<R> selection(Func.MaybeBiFunction<? super A, ? super B, ? extends O> select) {
    return selection((i, first, second) -> select.tryApply(first, second));
  }

  default <P extends Pair<A, B>> One.OfTwo<A, B> to(Func.MaybeBiFunction<? super A, ? super B, ? extends P> func) {
    return () -> cursor().map(pair -> func.tryApply(pair.first(), pair.second()));
  }

  default <O extends One.OfTwo<A, B>> One.OfTwo<A, B> too(Func.MaybeBiFunction<? super A, ? super B, ? extends O> func) {
    return () -> cursor().flatMap(pair -> func.tryApply(pair.first(), pair.second()).cursor());
  }
}
