package io.artoo.query.many;

import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.At;
import io.artoo.query.many.impl.Unique;


import java.util.function.Predicate;

import static io.artoo.type.Nullability.nonNullable;

public interface Uniquable<T extends Record> extends Queryable<T> {
  default One<T> at(final int index) {
    return new At<>(this, index)::iterator;
  }

  default One<T> first() {
    return new Unique<>(this, (i, it) -> {}, true, false, it -> true)::iterator;
  }

  default One<T> first(final Predicate<? super T> where) {
    return new Unique<>(this, (i, it) -> {}, true, false, nonNullable(where, "where"))::iterator;
  }

  default One<T> last() {
    return last(it -> true);
  }

  default One<T> last(final Predicate<? super T> where) {
    return new Unique<>(this, (i, it) -> {}, false, false, nonNullable(where, "where"))::iterator;
  }

  default One<T> single() {
    return single(it -> true);
  }

  default One<T> single(final Predicate<? super T> where) {
    return new Unique<>(this, (i, it) -> {}, false, true, nonNullable(where, "where"))::iterator;
  }
}
