package io.artoo.query.many;

import io.artoo.func.Cons;
import io.artoo.func.Pred;
import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Aggregate;

import static io.artoo.func.Func.identity;
import static io.artoo.func.Pred.tautology;

interface Countable<T> extends Queryable<T> {
  default One<Integer> count() {
    return this.count(tautology());
  }

  default One<Integer> count(final Pred<? super T> where) {
    return new Aggregate<>(this, Cons.nothing(), 0, where, identity(), (count, item) -> count + 1)::iterator;
  }
}
