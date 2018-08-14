package oak.collect.query.aggregate;

import oak.collect.cursor.Cursor;
import oak.func.fun.Function2;
import oak.collect.query.Queryable;

import java.util.Iterator;

final class Seed<S, R> implements Aggregation<R> {
  private final Queryable<S> some;
  private final R seed;
  private final Function2<R, S, R> reduce;

  Seed(final Queryable<S> some, final R seed, final Function2<R, S, R> reduce) {
    this.some = some;
    this.seed = seed;
    this.reduce = reduce;
  }

  @Override
  public final Iterator<R> iterator() {
    var reduced = seed;
    for (final var value : some) {
      reduced = reduce.apply(reduced, value);
    }
    return Cursor.maybe(reduced);
  }
}
