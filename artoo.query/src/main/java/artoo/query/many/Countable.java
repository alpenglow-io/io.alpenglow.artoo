package artoo.query.many;

import artoo.func.Cons;
import artoo.func.Pred;
import artoo.query.One;
import artoo.query.Queryable;
import artoo.query.many.impl.Aggregate;

import static artoo.func.Func.identity;
import static artoo.func.Pred.tautology;

interface Countable<T> extends Queryable<T> {
  default One<Integer> count() {
    return this.count(tautology());
  }

  default One<Integer> count(final Pred<? super T> where) {
    return new Aggregate<>(this, Cons.nothing(), 0, where, identity(), (count, item) -> count + 1)::iterator;
  }
}
