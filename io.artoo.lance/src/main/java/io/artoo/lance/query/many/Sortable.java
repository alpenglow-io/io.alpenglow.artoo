package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

public interface Sortable<T> extends Queryable<T> {
  default Many<T> order() {
    return Many.of(cursor().order());
  }
}
