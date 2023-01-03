package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;

public interface Sortable<T> extends Queryable<T> {
  default Ordering<T> order() {
    return new Order<>(this);
  }
}
