package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.oper.Count;

interface Countable<T> extends Queryable<T> {
  default One<Integer> count() {
    return count(it -> true);
  }

  default One<Integer> count(final Pred.Uni<? super T> where) {
    return () -> cursor().map(new Count<>(where)).scroll();
  }
}

