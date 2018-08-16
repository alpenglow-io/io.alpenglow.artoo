package oak.collect.query.project;

import oak.collect.query.Maybe;

final class SelectJust<R, S> implements Maybe<R> {
  private final Maybe<Maybe<R>> maybes;

  SelectJust(final Maybe<Maybe<R>> maybes) {
    this.maybes = maybes;
  }

  @Override
  public final R get() {
    return maybes.iterator().hasNext()
      ? maybes.iterator().next().iterator().next()
      : null;
  }
}
