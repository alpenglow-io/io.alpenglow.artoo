package artoo.query.many;

import artoo.func.Pred;
import artoo.query.Many;
import artoo.query.Queryable;
import artoo.query.many.impl.Distinct;

import static artoo.func.$2.ConsInt.nothing;
import static artoo.func.Pred.tautology;
import static artoo.type.Nullability.nonNullable;

public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return distinct(tautology());
  }

  default Many<T> distinct(final Pred<? super T> where) {
    return new Distinct<>(this, nothing(), nonNullable(where, "where"))::iterator;
  }
}
