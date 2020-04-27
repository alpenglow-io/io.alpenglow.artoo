package artoo.query.many;

import artoo.func.$2.ConsInt;
import artoo.query.Many;
import artoo.query.Queryable;
import artoo.query.many.impl.Concat;

import static artoo.type.Nullability.nonNullable;

public interface Concatenatable<T> extends Queryable<T> {
  default <Q extends Queryable<T>> Many<T> concat(final Q queryable) {
    return new Concat<>(this, ConsInt.nothing(), nonNullable(queryable, "queryable"))::iterator;
  }
}
