package oak.query.many;

import oak.func.$2.ConsInt;
import oak.query.Many;
import oak.query.Queryable;
import oak.query.many.internal.Concat;

import static oak.type.Nullability.nonNullable;

public interface Concatenatable<T> extends Queryable<T> {
  default <Q extends Queryable<T>> Many<T> concat(final Q queryable) {
    return new Concat<>(this, ConsInt.nothing(), nonNullable(queryable, "queryable"))::iterator;
  }
}
