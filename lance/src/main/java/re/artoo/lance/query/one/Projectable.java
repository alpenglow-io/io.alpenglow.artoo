package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.Select;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final TryFunction1<? super T, ? extends R> function) {
    return () -> cursor().map(Select.plain(function));
  }

  default <R, Q extends Queryable<R>> Many<R> selection(final TryFunction1<? super T, ? extends Q> function) {
    return () -> cursor().map(Select.plain(function)).flatMap(Queryable::cursor);
  }

  default <R> One<R> select(final TrySupplier1<? extends R> supplier) {
    return () -> cursor().map(Select.plain(it -> supplier.get()));
  }
}
