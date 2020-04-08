package trydent.query.many;

import trydent.func.Pred;
import trydent.query.One;
import trydent.query.Queryable;
import trydent.query.many.internal.At;
import trydent.query.many.internal.Unique;

import static trydent.func.$2.ConsInt.nothing;
import static trydent.func.Pred.tautology;
import static trydent.type.Nullability.nonNullable;

public interface Uniquable<T> extends Queryable<T> {
  default One<T> at(final int index) {
    return new At<>(this, index)::iterator;
  }

  default One<T> first() {
    return new Unique<>(this, nothing(), true, false, tautology())::iterator;
  }

  default One<T> first(final Pred<? super T> where) {
    return new Unique<>(this, nothing(), true, false, nonNullable(where, "where"))::iterator;
  }

  default One<T> last() {
    return last(tautology());
  }

  default One<T> last(final Pred<? super T> where) {
    return new Unique<>(this, nothing(), false, false, nonNullable(where, "where"))::iterator;
  }

  default One<T> single() {
    return single(tautology());
  }

  default One<T> single(final Pred<? super T> where) {
    return new Unique<>(this, nothing(), false, true, nonNullable(where, "where"))::iterator;
  }
}
