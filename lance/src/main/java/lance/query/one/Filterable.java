package lance.query.one;

import lance.func.Pred;
import lance.query.One;
import lance.Queryable;
import lance.query.func.NotOfType;
import lance.query.func.OfType;
import lance.query.func.Where;

public interface Filterable<T> extends Queryable<T> {
  default One<T> where(final Pred.MaybePredicate<? super T> where) {
    return () -> cursor().map(new Where<>((i, it) -> where.tryTest(it)));
  }

  default <R> One<R> ofType(final Class<? extends R> type) {
    return () -> cursor().map(new OfType<>(type));
  }

  default <R> One<T> notOfType(final Class<? extends R> type) {
    return () -> cursor().map(new NotOfType<>(type));
  }
}
