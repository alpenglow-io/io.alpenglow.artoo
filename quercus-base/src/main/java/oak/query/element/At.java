package oak.query.element;

import oak.query.Maybe;
import oak.query.Queryable;

final class At<T> implements Element<T, Maybe<T>>, Maybe<T> {
  private final Queryable<T> some;
  private final long index;

  At(final Queryable<T> some, final long index) {
    this.some = some;
    this.index = index;
  }

  @Override
  public final T get() {
    var i = index;
    T value = null;
    for (var iterator = some.iterator(); i >= 0; i--) {
      value = iterator.hasNext() ? iterator.next() : null;
    }
    return value;
  }
}
