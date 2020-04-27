package artoo.query.many;

import artoo.func.$2.ConsInt;
import artoo.func.$2.PredInt;
import artoo.func.Pred;
import artoo.query.Many;
import artoo.query.Queryable;
import artoo.query.many.impl.Partition;

import static artoo.func.$2.PredInt.not;
import static artoo.type.Nullability.nonNullable;

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

