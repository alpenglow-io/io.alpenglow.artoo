package re.artoo.lance.query.one.oftwo;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryFunction3;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.Select;
import re.artoo.lance.tuple.Pair;

public interface Projectable<A, B> extends Queryable.OfTwo<A, B> {
  default <R> One<R> select(TryFunction3<? super Integer, ? super A, ? super B, ? extends R> select) {
    return () -> cursor().map(Select.indexed((index, record) -> select.apply(index, record.first(), record.second())));
  }

  default <R> One<R> select(TryFunction2<? super A, ? super B, ? extends R> select) {
    return () -> cursor().map(pair -> select.invoke(pair.first(), pair.second()));
  }

  default <R, O extends One<R>> One<R> selection(TryFunction3<? super Integer, ? super A, ? super B, ? extends O> select) {
    return () -> cursor().map(Select.indexed((index, pair) -> select.invoke(index, pair.first(), pair.second()))).flatMap(Queryable::cursor);
  }

  default <R, O extends One<R>> One<R> selection(TryFunction2<? super A, ? super B, ? extends O> select) {
    return () -> cursor().map(pair -> select.invoke(pair.first(), pair.second())).flatMap(Queryable::cursor);
  }

  default <P extends Pair<A, B>> One.OfTwo<A, B> to(TryFunction2<? super A, ? super B, ? extends P> func) {
    return () -> cursor().map(pair -> func.invoke(pair.first(), pair.second()));
  }

  default <O extends One.OfTwo<A, B>> One.OfTwo<A, B> too(TryFunction2<? super A, ? super B, ? extends O> func) {
    return () -> cursor().flatMap(pair -> func.invoke(pair.first(), pair.second()).cursor());
  }
}
