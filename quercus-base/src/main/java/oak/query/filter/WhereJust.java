package oak.query.filter;

import oak.collect.cursor.Cursor;
import oak.func.pre.Predicate1;
import oak.query.Maybe;

import java.util.Iterator;

final class WhereJust<S> implements Filtering<S, Maybe<S>>, Maybe<S> {
  private final Maybe<S> maybe;
  private final Predicate1<S> filter;

  WhereJust(final Maybe<S> maybe, final Predicate1<S> filter) {
    this.maybe = maybe;
    this.filter = filter;
  }

  @Override
  public final Iterator<S> iterator() {
    return Cursor.maybe(maybe.iterator().next());
  }
}
