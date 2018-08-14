package oak.collect.query.project;

import oak.collect.cursor.Cursor;
import oak.func.fun.Function1;
import oak.collect.query.Maybe;

import java.util.Iterator;

final class SelectMaybe<S, R> implements Maybe<R> {
  private final Maybe<S> maybe;
  private final Function1<S, R> map;

  SelectMaybe(final Maybe<S> maybe, Function1<S, R> map) {
    this.maybe = maybe;
    this.map = map;
  }

  @Override
  public final Iterator<R> iterator() {
    return maybe.iterator().hasNext()
      ? Cursor.once(map.apply(maybe.iterator().next()))
      : Cursor.none();
  }
}
