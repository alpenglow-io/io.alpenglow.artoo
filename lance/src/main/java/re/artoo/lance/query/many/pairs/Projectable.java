package re.artoo.lance.query.many.pairs;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryFunction3;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.closure.Select;
import re.artoo.lance.tuple.Pair;

public interface Projectable<A, B> extends Queryable.OfTwo<A, B> {
  default <R> Many<R> select(TryFunction3<? super Integer, ? super A, ? super B, ? extends R> select) {
    return () -> cursor().map(Select.indexed((index, pair) -> select.apply(index, pair.first(), pair.second())));
  }

  default <R> Many<R> select(TryFunction2<? super A, ? super B, ? extends R> select) {
    return select((index, first, second) -> select.invoke(first, second));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(TryFunction3<? super Integer, ? super A, ? super B, ? extends Q> select) {
    return () -> cursor().map(Select.indexed((i, pair) -> select.invoke(i, pair.first(), pair.second()))).flatMap(Queryable::cursor);
  }

  default <R, Q extends Queryable<R>> Many<R> selection(TryFunction2<? super A, ? super B, ? extends Q> select) {
    return () -> cursor().map(pair -> select.invoke(pair.first(), pair.second())).flatMap(Queryable::cursor);
  }

  default <P extends Pair<A, B>> Many.Pairs<A, B> to(TryFunction2<? super A, ? super B, ? extends P> select) {
    return () -> cursor().map(pair -> select.invoke(pair.first(), pair.second()));
  }

  default <Q extends Queryable.OfTwo<A, B>> Many.Pairs<A, B> too(TryFunction2<? super A, ? super B, ? extends Q> func) {
    return () -> cursor().flatMap(pair -> func.invoke(pair.first(), pair.second()).cursor());
  }

}
