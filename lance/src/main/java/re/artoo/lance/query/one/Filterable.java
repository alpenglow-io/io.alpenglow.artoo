package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.One;

import static re.artoo.lance.func.TryPredicate1.not;

public interface Filterable<T> extends Queryable<T> {
  default One<T> where(final TryPredicate1<? super T> where) {
    return () -> cursor().filter(where);
  }

  default <R> One<R> ofType(final Class<? extends R> type) {
    return () -> cursor().filter(type::isInstance).map(type::cast);
  }

  default <R> One<T> notOfType(final Class<? extends R> type) {
    return () -> cursor().filter(not(type::isInstance));
  }
}
