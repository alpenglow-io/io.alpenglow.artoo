package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.closure.Select;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(TryFunction2<? super Integer, ? super T, ? extends R> select) {
    return () -> cursor().map(Select.indexed(select));
  }

  default <R> Many<R> select(TryFunction1<? super T, ? extends R> select) {
    return () -> cursor().map(it -> select.invoke(it));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(TryFunction2<? super Integer, ? super T, ? extends Q> select) {
    return () -> cursor().map(Select.indexed(select)).flatMap(Queryable::cursor);
  }

  default <R, Q extends Queryable<R>> Many<R> selection(TryFunction1<? super T, ? extends Q> select) {
    return () -> cursor().map(select::invoke).flatMap(Queryable::cursor);
  }
}
