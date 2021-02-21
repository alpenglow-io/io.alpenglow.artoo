package io.artoo.lance.query.many;

import io.artoo.lance.literator.cursor.routine.Routine;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.scope.Nullability.nonNullable;

public interface Concatenatable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> concat(final T... elements) {
    return () -> cursor().to(Routine.concat(elements));
  }

  default <Q extends Queryable<T>> Many<T> concat(final Q queryable) {
    return () -> cursor().to(Routine.concat(queryable.cursor()));
  }
}

