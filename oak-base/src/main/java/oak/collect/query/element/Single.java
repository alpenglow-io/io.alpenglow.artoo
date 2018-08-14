package oak.collect.query.element;

import oak.collect.query.Maybe;
import oak.collect.query.Queryable;

final class Single<S> implements Element<S, Maybe<S>>, Maybe<S> {
  private final Queryable<S> some;

  Single(final Queryable<S> some) {
    this.some = some;
  }

  @Override
  public final S get() {
    final var iterator = some.iterator();
    return iterator.hasNext() ? iterator.next() : null;
  }
}
