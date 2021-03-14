package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.query.Queryable;

public interface Sortable<T> extends Queryable<T> {
  default Ordering<T> order() {
    return new Order<>(this);
  }
}
