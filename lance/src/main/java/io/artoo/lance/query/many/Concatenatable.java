package io.artoo.lance.query.many;

import io.artoo.lance.literator.cursor.routine.concat.Concat;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

public interface Concatenatable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> concat(final T... elements) {
    return () -> cursor().to(Concat.array(elements));
  }

  default <Q extends Queryable<T>> Many<T> concat(final Q queryable) {
    return () -> cursor().to(Concat.liter(queryable.cursor()));
  }
}

