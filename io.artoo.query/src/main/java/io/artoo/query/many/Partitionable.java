package io.artoo.query.many;

import io.artoo.query.Many;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Partition;


import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static io.artoo.type.Nullability.nonNullable;
import static java.util.function.Predicate.not;

public interface Partitionable<T extends Record> extends Queryable<T> {
  default Many<T> skip(final int until) {
    return skipWhile((index, it) -> index < until);
  }

  default Many<T> skipWhile(final Predicate<? super T> where) {
    return skipWhile((index, it) -> !where.test(it));
  }

  default Many<T> skipWhile(final BiPredicate<? super Integer, ? super T> where) {
    nonNullable(where, "where");
    return new Partition<>(this, (i, it) -> {}, where.negate())::iterator;
  }

  default Many<T> take(final int until) {
    return new Partition<>(this, (i, it) -> {}, (index, it) -> index < until)::iterator;
  }

  default Many<T> takeWhile(final Predicate<? super T> where) {
    nonNullable(where, "where");
    return takeWhile((index, param) -> where.test(param));
  }

  default Many<T> takeWhile(final BiPredicate<? super Integer, ? super T> where) {
    return new Partition<>(this, (i, it) -> {}, nonNullable(where, "where"))::iterator;
  }
}

