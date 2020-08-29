package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Concatenatable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> concat(final T... elements) {
    return concat(Many.from(nonNullable(elements, "elements")));
  }

  default <Q extends Queryable<T>> Many<T> concat(final Q queryable) {
    return Many.wany(cursor().concat(queryable.cursor()));
  }
}

