package oak.query.element;

import oak.query.Maybe.MaybeFunction1;
import oak.query.Queryable;
import oak.query.aggregate.Aggregation;

import java.util.Iterator;

final class Last<S> implements Element<S, Queryable<S>>, Queryable<S> {
  private final Aggregation<Integer> count;
  private final MaybeFunction1<Integer, S> last;

  Last(final Aggregation<Integer> count, final MaybeFunction1<Integer, S> last) {
    this.count = count;
    this.last = last;
  }

  @Override
  public final Iterator<S> iterator() {
    return count.selectJust(last)
      .iterator();
  }
}
