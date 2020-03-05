package oak.query.many;

import oak.query.Queryable;
import oak.query.One;
import oak.query.many.internal.Aggregate;
import oak.type.Numeric;

import static oak.func.Cons.nothing;
import static oak.func.Func.identity;
import static oak.func.Pred.tautology;
import static oak.type.Nullability.nonNullable;
import static oak.type.Numeric.asNumber;

interface Summable<T> extends Queryable<T> {
  default <V, N extends Number> One<N> sum(final oak.func.Func<? super T, ? extends V> select, final oak.func.Func<? super V, ? extends N> asNumber) {
    nonNullable(select, "select");
    nonNullable(asNumber, "asNumber");
    return new Aggregate<T, N, N>(this, nothing(), null, tautology(), value -> select.andThen(asNumber).apply(value), Numeric::sum)::iterator;
  }

  default <V, N extends Number> One<N> sum(final oak.func.Func<? super T, ? extends V> select) {
    return this.<V, N>sum(select, asNumber());
  }

  default <N extends Number> One<N> sum() {
    return this.<T, N>sum(identity(), asNumber());
  }
}
