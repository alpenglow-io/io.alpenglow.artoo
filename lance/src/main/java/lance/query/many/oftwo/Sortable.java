package lance.query.many.oftwo;

import lance.Queryable;

public interface Sortable<A, B> extends Queryable.OfTwo<A, B> {
  default Ordering<A, B> order() {
    return new Order<>(this);
  }
}
