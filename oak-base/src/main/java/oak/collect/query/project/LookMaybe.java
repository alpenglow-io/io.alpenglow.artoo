package oak.collect.query.project;

import oak.func.con.Consumer1;
import oak.collect.query.Maybe;

import java.util.Iterator;

final class LookMaybe<S> implements Maybe<S> {
  private final Maybe<S> maybe;
  private final Consumer1<S> peek;

  LookMaybe(final Maybe<S> maybe, final Consumer1<S> peek) {
    this.maybe = maybe;
    this.peek = peek;
  }

  @Override
  public final S get() {
    if (maybe.iterator().hasNext()) peek.accept(maybe.iterator().next());
    return maybe.iterator().next();
  }
}
