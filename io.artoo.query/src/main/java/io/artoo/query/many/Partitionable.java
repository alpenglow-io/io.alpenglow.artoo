package io.artoo.query.many;

import io.artoo.func.$2.ConsInt;
import io.artoo.func.$2.PredInt;
import io.artoo.func.Pred;
import io.artoo.query.Many;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Partition;

import static io.artoo.func.$2.PredInt.not;
import static io.artoo.type.Nullability.nonNullable;

public interface Partitionable<T> extends Queryable<T> {
  default Many<T> skip(final int until) {
    return skipWhile((index, it) -> index < until);
  }

  default Many<T> skipWhile(final Pred<? super T> where) {
    return skipWhile((index, it) -> !where.apply(it));
  }

  default Many<T> skipWhile(final PredInt<? super T> where) {
    return new Partition<>(this, ConsInt.nothing(), not(nonNullable(where, "where")))::iterator;
  }

  default Many<T> take(final int until) {
    return new Partition<>(this, ConsInt.nothing(), (index, it) -> index < until)::iterator;
  }

  default Many<T> takeWhile(final Pred<? super T> where) {
    nonNullable(where, "where");
    return takeWhile((index, param) -> where.test(param));
  }

  default Many<T> takeWhile(final PredInt<? super T> where) {
    return new Partition<>(this, ConsInt.nothing(), nonNullable(where, "where"))::iterator;
  }
}

