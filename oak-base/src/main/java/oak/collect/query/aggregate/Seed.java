package oak.collect.query.aggregate;

import oak.collect.query.Queryable;
import oak.func.fun.Function2;

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
  public final R get() {
    var reduced = seed;
    for (final var value : some) {
      reduced = reduce.apply(reduced, value);
    }
    return reduced;
  }
}
