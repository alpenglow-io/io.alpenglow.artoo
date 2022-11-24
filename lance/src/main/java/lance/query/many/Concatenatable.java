package lance.query.many;

import lance.literator.cursor.routine.concat.Concat;
import lance.query.Many;
import lance.Queryable;

public interface Concatenatable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> concat(final T... elements) {
    return () -> cursor().to(Concat.array(elements));
  }

  default <Q extends Queryable<T>> Many<T> concat(final Q queryable) {
    return () -> cursor().to(Concat.liter(queryable.cursor()));
  }
}

