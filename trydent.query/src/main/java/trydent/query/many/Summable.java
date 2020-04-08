package trydent.query.many;

import trydent.query.Queryable;
import trydent.query.One;
import trydent.query.many.internal.Aggregate;
import trydent.type.Numeric;

import static trydent.func.Cons.nothing;
import static trydent.func.Func.identity;
import static trydent.func.Pred.tautology;
import static trydent.type.Nullability.nonNullable;
import static trydent.type.Numeric.asNumber;

interface Summable<T> extends Queryable<T> {
  default <V, N extends Number> One<N> sum(final trydent.func.Func<? super T, ? extends V> select, final trydent.func.Func<? super V, ? extends N> asNumber) {
    nonNullable(select, "select");
    nonNullable(asNumber, "asNumber");
    return new Aggregate<T, N, N>(this, nothing(), null, tautology(), value -> select.andThen(asNumber).apply(value), Numeric::sum)::iterator;
  }

  default <V, N extends Number> One<N> sum(final trydent.func.Func<? super T, ? extends V> select) {
    return this.<V, N>sum(select, asNumber());
  }

  default <N extends Number> One<N> sum() {
    return this.<T, N>sum(identity(), asNumber());
  }
}
