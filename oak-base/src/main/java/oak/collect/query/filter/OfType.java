package oak.collect.query.filter;

import io.ibex.collect.seq.Sequence;
import oak.collect.query.Queryable;

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
    var typed = Sequence.<C>empty();
    for (final var value : some) {
      if (type.isInstance(value)) typed = typed.add(type.cast(value));
    }
    return typed.iterator();
  }
}
