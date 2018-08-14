package oak.collect.query.project;

import oak.collect.cursor.Cursor;
import oak.collect.query.Maybe;

import java.util.Iterator;

final class SelectJust<R, S> implements Maybe<R> {
  private final Maybe<Maybe<R>> maybes;

  SelectJust(final Maybe<Maybe<R>> maybes) {
    this.maybes = maybes;
  }

  @Override
  public final Iterator<R> iterator() {
    return maybes.iterator().hasNext()
      ? maybes.iterator().next().iterator()
      : Cursor.none();
  }
}
