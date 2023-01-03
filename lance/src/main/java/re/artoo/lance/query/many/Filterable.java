package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.closure.NotOfType;
import re.artoo.lance.query.closure.OfType;
import re.artoo.lance.query.closure.Where;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final TryPredicate1<? super T> where) {
    return where((index, param) -> where.invoke(param));
  }

  default Many<T> where(final TryPredicate2<? super Integer, ? super T> where) {
    return () -> cursor().map(new Where<>(where));
  }

  default <R> Many<R> ofType(final Class<? extends R> type) {
    return () -> cursor().map(new OfType<>(type));
  }
  default <R> Many<T> notOfType(final Class<? extends R> type) {
    return () -> cursor().map(new NotOfType<>(type));
  }
}

