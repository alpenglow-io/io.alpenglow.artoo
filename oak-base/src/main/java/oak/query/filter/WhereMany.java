package oak.query.filter;

import io.ibex.collect.seq.Sequence;
import oak.func.pre.Predicate1;
import oak.query.Queryable;

import java.util.Iterator;

final class WhereMany<T> implements Filtering<T, Queryable<T>>, Queryable<T> {
  private final Queryable<T> some;
  private final Predicate1<T> filter;

  WhereMany(final Queryable<T> some, final Predicate1<T> filter) {
    this.some = some;
    this.filter = filter;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Iterator<T> iterator() {
    var filtered = Sequence.<T>empty();
    for (final var value : some) {
      if (filter.test(value)) filtered = filtered.add(value);
    }
    return filtered.iterator();
  }
}
