package artoo.query.many;

import artoo.func.Pred;
import artoo.query.One;
import artoo.query.Queryable;
import artoo.query.many.impl.At;
import artoo.query.many.impl.Unique;

import static artoo.func.$2.ConsInt.nothing;
import static artoo.func.Pred.tautology;
import static artoo.type.Nullability.nonNullable;

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
