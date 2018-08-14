package oak.query.aggregate;

import oak.collect.cursor.Cursor;
import oak.query.Queryable;

import java.util.Iterator;

final class Count<T> implements Aggregation<Integer> {
  private final Queryable<T> many;

  Count(final Queryable<T> many) {
    this.many = many;
  }

  @Override
  public final Iterator<Integer> iterator() {
    var count = 0;
    for (final var ignored : many) count++;
    return Cursor.once(count);
  }
}
