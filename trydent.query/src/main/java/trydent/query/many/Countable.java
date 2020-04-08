package trydent.query.many;

import trydent.func.Cons;
import trydent.func.Pred;
import trydent.query.One;
import trydent.query.Queryable;
import trydent.query.many.internal.Aggregate;

import static trydent.func.Func.identity;
import static trydent.func.Pred.tautology;

interface Countable<T> extends Queryable<T> {
  default One<Integer> count() {
    return this.count(tautology());
  }

  default One<Integer> count(final Pred<? super T> where) {
    return new Aggregate<>(this, Cons.nothing(), 0, where, identity(), (count, item) -> count + 1)::iterator;
  }
}
