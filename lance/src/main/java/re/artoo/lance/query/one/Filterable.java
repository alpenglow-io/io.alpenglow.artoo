package re.artoo.lance.query.one;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.One;
import re.artoo.lance.query.closure.NotOfType;
import re.artoo.lance.query.closure.OfType;
import re.artoo.lance.query.closure.Where;

public interface Filterable<T> extends Queryable<T> {
  default One<T> where(final TryPredicate1<? super T> where) {
    return () -> cursor().filter(where);
  }

  default <R> One<R> ofType(final Class<? extends R> type) {
    return () -> cursor().map(new OfType<>(type));
  }

  default <R> One<T> notOfType(final Class<? extends R> type) {
    return () -> cursor().map(new NotOfType<>(type));
  }
}
