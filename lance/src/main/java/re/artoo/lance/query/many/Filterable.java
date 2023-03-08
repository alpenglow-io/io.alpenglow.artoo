package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.closure.NotOfType;

public interface Filterable<T> extends Queryable<T> {
  default Many<T> where(final TryPredicate1<? super T> where) {
    return where((index, param) -> where.invoke(param));
  }

  default Many<T> where(final TryIntPredicate1<? super T> where) {
    return () -> cursor().filter(where);
  }

  @SuppressWarnings("unchecked")
  default <R> Many<R> ofType() {
    return ofComponentType();
  }

  @SuppressWarnings("unchecked")
  private <R> Many<R> ofComponentType(R... type) {
    return () -> cursor()
      .filter(it -> type.getClass().componentType().isInstance(it))
      .map(it -> (R) type.getClass().componentType().cast(it));
  }
  default <R> Many<T> notOfType(final Class<? extends R> type) {
    return () -> cursor().map(new NotOfType<>(type));
  }
  default Many<T> coalesce() { return () -> cursor().onPresenceOnly(); }
}

