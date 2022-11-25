package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.literator.cursor.routine.concat.Concat;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.Queryable;

public interface Concatenatable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> concat(final T... elements) {
    return () -> cursor().to(Concat.array(elements));
  }

  default <Q extends Queryable<T>> Many<T> concat(final Q queryable) {
    return () -> cursor().to(Concat.liter(queryable.cursor()));
  }
}

