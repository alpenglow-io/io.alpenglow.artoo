package trydent.query.many;

import trydent.func.Pred;
import trydent.query.Many;
import trydent.query.Queryable;
import trydent.query.many.internal.Distinct;

import static trydent.func.$2.ConsInt.nothing;
import static trydent.func.Pred.tautology;
import static trydent.type.Nullability.nonNullable;

public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return distinct(tautology());
  }

  default Many<T> distinct(final Pred<? super T> where) {
    return new Distinct<>(this, nothing(), nonNullable(where, "where"))::iterator;
  }
}
