package oak.query;

import oak.collect.cursor.Cursor;

import java.util.Iterator;

final class Empty<Q> implements Queryable<Q> {
  @Override
  public final Iterator<Q> iterator() {
    return Cursor.none();
  }
}
