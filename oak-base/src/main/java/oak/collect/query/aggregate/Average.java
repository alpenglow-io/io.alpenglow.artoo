package oak.collect.query.aggregate;

import oak.collect.query.Maybe;
import oak.collect.query.Queryable;
import oak.collect.query.aggregate.mean.Mean;

final class Average<T> implements Maybe<T> {
  private final Queryable<T> some;

  Average(Queryable<T> some) {
    this.some = some;
  }

  @Override
  public final T get() {
    return ClosestMean.of(some, Mean.withHashCode(some)).get();
  }
}
