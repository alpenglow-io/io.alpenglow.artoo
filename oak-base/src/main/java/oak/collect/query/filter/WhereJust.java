package oak.collect.query.filter;

import oak.collect.cursor.Cursor;
import oak.collect.query.Maybe;
import oak.func.pre.Predicate1;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

final class WhereJust<S> implements Filtering<S>, Maybe<S> {
  private final Maybe<S> maybe;
  private final Predicate1<S> filter;

  WhereJust(final Maybe<S> maybe, final Predicate1<S> filter) {
    this.maybe = maybe;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<S> iterator() {
    for (var value : maybe) {
      return filter.test(value) ? Cursor.maybe(value) : Cursor.none();
    }
    return Cursor.none();
  }
}
