package io.artoo.query.many;


import io.artoo.query.Many;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Insert;

import static io.artoo.type.Nullability.nonNullable;

public interface Insertable<T extends Record> extends Queryable<T> {
  @SuppressWarnings("unchecked")
  default Many<T> insert(final T... values) {
    return new Insert<>(this, (i, it) -> {}, Many.from(nonNullable(values, "values")))::iterator;
  }

  default <Q extends Queryable<T>> Many<T> insert(final Q queryable) {
    return new Insert<>(this, (i, it) -> {}, nonNullable(queryable, "queryable"))::iterator;
  }
}
