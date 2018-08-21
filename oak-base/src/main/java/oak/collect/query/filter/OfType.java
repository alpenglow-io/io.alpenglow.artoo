package oak.collect.query.filter;

import oak.collect.query.Queryable;

import java.util.ArrayList;
import java.util.Iterator;

final class OfType<T, C> implements Filtering<C, Queryable<C>>, Queryable<C> {
  private final Queryable<T> some;
  private final Class<C> type;

  OfType(final Queryable<T> some, final Class<C> type) {
    this.some = some;
    this.type = type;
  }

  @Override
  public final Iterator<C> iterator() {
    var typeds = new ArrayList<C>();
    for (final var value : some) {
      if (type.isInstance(value)) typeds.add(type.cast(value));
    }
    return typeds.iterator();
  }
}
