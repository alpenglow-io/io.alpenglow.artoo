package io.artoo.query.many;

import io.artoo.query.Many;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Distinct;


import java.util.function.Predicate;

import static io.artoo.type.Nullability.nonNullable;

public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return distinct(it -> true);
  }

  default Many<T> distinct(final Predicate<? super T> where) {
    return new Distinct<>(this, (i, it) -> {}, nonNullable(where, "where"))::iterator;
  }
}
