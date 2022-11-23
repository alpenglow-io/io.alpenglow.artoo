package io.artoo.lance.query.many.oftwo;

import io.artoo.lance.query.Queryable;

public interface Sortable<A, B> extends Queryable.OfTwo<A, B> {
  default Ordering<A, B> order() {
    return new Order<>(this);
  }
}
