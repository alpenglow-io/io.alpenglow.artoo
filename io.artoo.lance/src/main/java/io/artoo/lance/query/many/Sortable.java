package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.fetcher.routine.Routine.sort;

public interface Sortable<T> extends Queryable<T> {
  default Many<T> order() {
    return () -> cursor().invoke(sort());
  }
}
