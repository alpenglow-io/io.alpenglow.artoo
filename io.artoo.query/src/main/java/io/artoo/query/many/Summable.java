package io.artoo.query.many;

import io.artoo.func.Func;
import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Aggregate;
import io.artoo.type.Numeric;

import static io.artoo.func.Cons.nothing;
import static io.artoo.func.Func.identity;
import static io.artoo.func.Pred.tautology;
import static io.artoo.type.Nullability.nonNullable;
import static io.artoo.type.Numeric.asNumber;

interface Summable<T> extends Queryable<T> {
  default <V, N extends Number> One<N> sum(final Func<? super T, ? extends V> select, final Func<? super V, ? extends N> asNumber) {
    nonNullable(select, "select");
    nonNullable(asNumber, "asNumber");
    return new Aggregate<T, N, N>(this, nothing(), null, tautology(), value -> select.after(asNumber).apply(value), Numeric::sum)::iterator;
  }

  default <V, N extends Number> One<N> sum(final Func<? super T, ? extends V> select) {
    return this.<V, N>sum(select, asNumber());
  }

  default <N extends Number> One<N> sum() {
    return this.<T, N>sum(identity(), asNumber());
  }
}
