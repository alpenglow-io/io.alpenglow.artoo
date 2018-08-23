package oak.collect.query.aggregate;

import oak.collect.query.Queryable;
import oak.func.fun.Function1;
import oak.func.fun.Function2;

final class Identity<S, R> implements Aggregation<R> {
  private final Queryable<S> some;
  private final Function1<S, R> identity;
  private final Function2<R, S, R> reduce;

  Identity(final Queryable<S> some, final Function1<S, R> identity, final Function2<R, S, R> reduce) {
    this.some = some;
    this.identity = identity;
    this.reduce = reduce;
  }

  @Override
  public final R get() {
    var initial = true;
    R reduced = null;
    for (final var value : some) {
      if (initial) {
        reduced = identity.apply(value);
        initial = false;
      } else {
        reduced = reduce.apply(reduced, value);
      }
    }
    return reduced;
  }
}
