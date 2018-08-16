package oak.collect.query.project;

import oak.collect.query.Maybe;
import oak.func.fun.Function1;

final class SelectMaybe<S, R> implements Maybe<R> {
  private final Maybe<S> maybe;
  private final Function1<S, R> map;

  SelectMaybe(final Maybe<S> maybe, Function1<S, R> map) {
    this.maybe = maybe;
    this.map = map;
  }

  @Override
  public final R get() {
    return maybe.iterator().hasNext()
      ? map.apply(maybe.iterator().next())
      : null;
  }
}
