package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.operation.Distinct;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return distinct(it -> true);
  }

  default Many<T> distinct(final Pred.Uni<? super T> where) {
    final var w = nonNullable(where, "where");
    return () -> cursor().map(new Distinct<T, T>(w).butNulls());
  }

  @SuppressWarnings("unchecked")
  default Many<T> union(final T... elements) {
    return union(Many.from(elements));
  }

  default <Q extends Queryable<T>> Many<T> union(final Q queryable) {
    return () -> cursor()
      .concat(queryable.cursor())
      .map(new Distinct<T, T>(it -> true));
  }
}

