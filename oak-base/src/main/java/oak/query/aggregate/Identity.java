package oak.query.aggregate;

import oak.collect.cursor.Cursor;
import oak.func.fun.Function1;
import oak.func.fun.Function2;
import oak.query.Queryable;

import java.util.Iterator;

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
  public final Iterator<R> iterator() {
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
    return Cursor.maybe(reduced);
  }
}
