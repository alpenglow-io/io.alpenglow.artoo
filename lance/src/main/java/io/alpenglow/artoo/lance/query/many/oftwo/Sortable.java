package io.alpenglow.artoo.lance.query.many.oftwo;

import io.alpenglow.artoo.lance.Queryable;

public interface Sortable<A, B> extends Queryable.OfTwo<A, B> {
  default Ordering<A, B> order() {
    return new Order<>(this);
  }
}
