package io.artoo.query.many;

import io.artoo.func.$2.ConsInt;
import io.artoo.query.Many;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Insert;

import static io.artoo.type.Nullability.nonNullable;

public interface Insertable<T> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> insert(final T... values) {
    return new Insert<>(this, ConsInt.nothing(), Many.from(nonNullable(values, "values")))::iterator;
  }

  default <Q extends Queryable<T>> Many<T> insert(final Q queryable) {
    return new Insert<>(this, ConsInt.nothing(), nonNullable(queryable, "queryable"))::iterator;
  }
}
