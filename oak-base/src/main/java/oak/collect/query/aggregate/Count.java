package oak.collect.query.aggregate;

import oak.collect.query.Queryable;

final class Count<T> implements Aggregation<Long> {
  private final Queryable<T> many;

  Count(final Queryable<T> many) {
    this.many = many;
  }

  @Override
  public final Long get() {
    var count = 0L;
    for (final var ignored : many) count++;
    return count;
  }
}
