package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

import static java.util.Arrays.sort;

public interface Sortable<T> extends Queryable<T> {
  default Many<T> order() {
    return () -> cursor().mapArray(elements -> {
      sort(elements);
      return elements;
    });
  }
}
