package oak.query.many;

import oak.func.Pred;
import oak.query.Queryable;
import oak.query.one.One;

import static oak.func.Cons.nothing;
import static oak.func.Func.identity;
import static oak.func.Pred.tautology;

interface Countable<T> extends Queryable<T> {
  default One<Long> count() {
    return this.count(tautology());
  }

  default One<Long> count(final Pred<? super T> where) {
    return new Aggregatable.Aggregate<>(this, nothing(), 0L, where, identity(), (count, item) -> count + 1);
  }
}
