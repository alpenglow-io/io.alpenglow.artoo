package trydent.query.many;

import trydent.func.$2.ConsInt;
import trydent.func.$2.PredInt;
import trydent.func.Pred;
import trydent.query.Many;
import trydent.query.Queryable;
import trydent.query.many.internal.Partition;

import static trydent.func.$2.PredInt.not;
import static trydent.type.Nullability.nonNullable;

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

