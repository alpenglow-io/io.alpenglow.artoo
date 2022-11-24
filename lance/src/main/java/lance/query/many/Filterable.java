package lance.query.many;

import lance.func.Pred;
import lance.query.Many;
import lance.Queryable;
import lance.query.func.NotOfType;
import lance.query.func.OfType;
import lance.query.func.Where;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final Pred.TryPredicate<? super T> where) {
    return where((index, param) -> where.tryTest(param));
  }

  default Many<T> where(final Pred.TryBiPredicate<? super Integer, ? super T> where) {
    return () -> cursor().map(new Where<>(where));
  }

  default <R> Many<R> ofType(final Class<? extends R> type) {
    return () -> cursor().map(new OfType<>(type));
  }

  default <R> Many<T> notOfType(final Class<? extends R> type) {
    return () -> cursor().map(new NotOfType<>(type));
  }
}

