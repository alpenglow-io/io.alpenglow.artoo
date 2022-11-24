package lance.query.many;

import lance.Queryable;

public interface Sortable<T> extends Queryable<T> {
  default Ordering<T> order() {
    return new Order<>(this);
  }
}
