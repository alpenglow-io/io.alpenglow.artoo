package io.artoo.query.many;

import io.artoo.func.$2.ConsInt;
import io.artoo.query.Many;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Concat;

import static io.artoo.type.Nullability.nonNullable;

public interface Concatenatable<T> extends Queryable<T> {
  default <Q extends Queryable<T>> Many<T> concat(final Q queryable) {
    return new Concat<>(this, ConsInt.nothing(), nonNullable(queryable, "queryable"))::iterator;
  }
}
