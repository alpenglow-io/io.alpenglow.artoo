package io.artoo.query.many;

import io.artoo.func.Pred;
import io.artoo.query.Many;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Distinct;

import static io.artoo.func.$2.ConsInt.nothing;
import static io.artoo.func.Pred.tautology;
import static io.artoo.type.Nullability.nonNullable;

public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return distinct(tautology());
  }

  default Many<T> distinct(final Pred<? super T> where) {
    return new Distinct<>(this, nothing(), nonNullable(where, "where"))::iterator;
  }
}
