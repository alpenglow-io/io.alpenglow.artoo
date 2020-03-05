package oak.query.many;

import oak.func.$2.IntCons;
import oak.query.Many;
import oak.query.Queryable;
import oak.query.many.internal.Concat;

import static oak.type.Nullability.nonNullable;

public interface Concatenatable<T> extends Queryable<T> {
  default <Q extends Queryable<T>> Many<T> concat(final Q queryable) {
    return new Concat<>(this, IntCons.nothing(), nonNullable(queryable, "queryable"))::iterator;
  }
}
