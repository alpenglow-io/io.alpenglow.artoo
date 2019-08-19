package oak.collect.query.filter;

import oak.func.pre.Predicate1;
import oak.collect.query.Queryable;

import java.util.ArrayList;
import java.util.Iterator;

final class WhereMany<T> implements Filtering<T>, Queryable<T> {
  private final Queryable<T> some;
  private final Predicate1<T> filter;

  WhereMany(final Queryable<T> some, final Predicate1<T> filter) {
    this.some = some;
    this.filter = filter;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Iterator<T> iterator() {
    var filtered = new ArrayList<T>();
    for (final var value : some) {
      if (filter.test(value)) filtered.add(value);
    }
    return filtered.iterator();
  }
}
