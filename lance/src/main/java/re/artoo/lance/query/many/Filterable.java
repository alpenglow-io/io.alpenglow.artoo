package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Many;

import static re.artoo.lance.func.TryPredicate1.not;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final TryPredicate1<? super T> where) {
    return where((index, param) -> where.invoke(param));
  }

  default Many<T> where(final TryIntPredicate1<? super T> where) {
    return () -> cursor().filter(where);
  }

  default <R> Many<R> ofType(Class<R> type) {
    return () -> cursor()
      .filter(type::isInstance)
      .map(type::cast);
  }

  default <R> Many<T> notOfType(final Class<? extends R> type) {
    return () -> cursor().filter(not(type::isInstance));
  }
  default Many<T> coalesce() { return () -> cursor().evaluable(); }
}

