package oak.collect.query.aggregate;

import oak.collect.query.Maybe;
import oak.collect.query.Queryable;

import java.util.Objects;

final class Average<T> implements Maybe<T> {
  private final Queryable<T> some;

  Average(Queryable<T> some) {
    this.some = some;
  }

  @Override
  public final T get() {
    var size = 0;
    int hashCode = 0;
    for (final var it : some) {
      hashCode += Objects.hashCode(it);
      size++;
    }
    hashCode /= size;
    T found = null;
    for (final var it : some) found = it.hashCode() <= hashCode ? it : found;
    return found;
  }
}
