package trydent.query.many;

import trydent.func.$2.ConsInt;
import trydent.query.Many;
import trydent.query.Queryable;
import trydent.query.many.internal.Concat;

import static trydent.type.Nullability.nonNullable;

public interface Concatenatable<T> extends Queryable<T> {
  default <Q extends Queryable<T>> Many<T> concat(final Q queryable) {
    return new Concat<>(this, ConsInt.nothing(), nonNullable(queryable, "queryable"))::iterator;
  }
}
