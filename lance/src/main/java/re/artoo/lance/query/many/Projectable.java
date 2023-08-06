package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Many;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(TryIntFunction1<? super T, ? extends R> select) {
    return () -> cursor().map(select);
  }

  default <R> Many<R> select(TryFunction1<? super T, ? extends R> select) {
    return () -> cursor().map(select);
  }

  default <R, Q extends Queryable<R>> Many<R> selection(TryIntFunction1<? super T, Q> select) {
    return () -> cursor().map(select).flatMap(Queryable::cursor);
  }

  default <R, Q extends Queryable<R>> Many<R> selection(TryFunction1<? super T, Q> select) {
    return () -> cursor().map(select).flatMap(Queryable::cursor);
  }
}
