package oak.collect.query.filter;

import oak.collect.query.Maybe;
import oak.func.pre.Predicate1;

final class WhereJust<S> implements Filtering<S, Maybe<S>>, Maybe<S> {
  private final Maybe<S> maybe;
  private final Predicate1<S> filter;

  WhereJust(final Maybe<S> maybe, final Predicate1<S> filter) {
    this.maybe = maybe;
    this.filter = filter;
  }

  @Override
  public final S get() {
    return maybe.get();
  }
}
