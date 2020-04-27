package artoo.query.many;

import artoo.func.Func;
import artoo.query.One;
import artoo.query.Queryable;
import artoo.query.many.impl.Aggregate;
import artoo.type.Numeric;

import static artoo.func.Cons.nothing;
import static artoo.func.Func.identity;
import static artoo.func.Pred.tautology;
import static artoo.type.Nullability.nonNullable;
import static artoo.type.Numeric.asNumber;

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
