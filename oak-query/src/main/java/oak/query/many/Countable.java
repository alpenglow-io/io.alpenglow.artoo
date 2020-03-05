package oak.query.many;

import oak.func.Cons;
import oak.func.Pred;
import oak.query.One;
import oak.query.Queryable;
import oak.query.many.internal.Aggregate;

import static oak.func.Func.identity;
import static oak.func.Pred.tautology;

interface Countable<T> extends Queryable<T> {
  default One<Integer> count() {
    return this.count(tautology());
  }

  default One<Integer> count(final Pred<? super T> where) {
    return new Aggregate<>(this, Cons.nothing(), 0, where, identity(), (count, item) -> count + 1)::iterator;
  }
}
