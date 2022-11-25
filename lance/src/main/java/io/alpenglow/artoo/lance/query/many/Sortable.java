package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.Queryable;

public interface Sortable<T> extends Queryable<T> {
  default Ordering<T> order() {
    return new Order<>(this);
  }
}
