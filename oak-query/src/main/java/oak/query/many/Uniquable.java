package oak.query.many;

import oak.func.Pred;
import oak.query.One;
import oak.query.Queryable;
import oak.query.many.internal.At;
import oak.query.many.internal.Unique;

import static oak.func.$2.ConsInt.nothing;
import static oak.func.Pred.tautology;
import static oak.type.Nullability.nonNullable;

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
