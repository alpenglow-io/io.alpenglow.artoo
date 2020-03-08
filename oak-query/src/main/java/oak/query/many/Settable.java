package oak.query.many;

import oak.func.Pred;
import oak.query.Many;
import oak.query.Queryable;
import oak.query.many.internal.Distinct;

import static oak.func.$2.ConsInt.nothing;
import static oak.func.Pred.tautology;
import static oak.type.Nullability.nonNullable;

public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return distinct(tautology());
  }

  default Many<T> distinct(final Pred<? super T> where) {
    return new Distinct<>(this, nothing(), nonNullable(where, "where"))::iterator;
  }
}
