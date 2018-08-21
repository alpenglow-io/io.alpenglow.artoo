package oak.collect.query.element;

import oak.collect.cursor.Cursor;
import oak.collect.query.Queryable;

import java.util.Iterator;

final class Last<S> implements Element<S, Queryable<S>>, Queryable<S> {
  private final Queryable<S> some;

  Last(final Queryable<S> some) {
    this.some = some;
  }

  @Override
  public final Iterator<S> iterator() {
    S last = null;
    for (final var one : some) last = one;
    return Cursor.maybe(last);
  }
}
