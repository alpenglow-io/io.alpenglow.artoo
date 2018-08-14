package oak.collect.query.aggregate;

import oak.collect.cursor.Cursor;
import oak.collect.query.Queryable;

import java.util.Iterator;

final class Count<T> implements Aggregation<Long> {
  private final Queryable<T> many;

  Count(final Queryable<T> many) {
    this.many = many;
  }

  @Override
  public final Iterator<Long> iterator() {
    var count = 0L;
    for (final var ignored : many) count++;
    return Cursor.once(count);
  }
}
