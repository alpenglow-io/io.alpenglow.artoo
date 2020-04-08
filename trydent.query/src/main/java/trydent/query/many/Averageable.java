package trydent.query.many;

import trydent.func.$2.ConsInt;
import trydent.func.$2.FuncInt;
import trydent.func.Func;
import trydent.query.Queryable;
import trydent.query.One;
import trydent.query.many.internal.Average;

import static trydent.func.$2.PredInt.tautology;
import static trydent.func.$2.FuncInt.identity;
import static trydent.type.Nullability.nonNullable;
import static trydent.type.Numeric.asDouble;
import static trydent.type.Numeric.asNumber;
import static trydent.type.Numeric.divide;

interface Averageable<T> extends Queryable<T> {
  default <V, N extends Number> One<N> average(final FuncInt<? super T, ? extends V> select, final trydent.func.Func<? super V, ? extends N> asNumber) {
    return new Average<T, V, N>(this, ConsInt.nothing(), tautology(), nonNullable(select, "select"), nonNullable(asNumber, "asNumber"))::iterator;
  }

  default <V, N extends Number> One<N> average(final Func<? super T, ? extends V> select, final trydent.func.Func<? super V, ? extends N> asNumber) {
    nonNullable(select, "select");
    nonNullable(asNumber, "asNumber");
    return average((index, it) -> select.apply(it), asNumber);
  }

  default <N extends Number> One<N> average(final FuncInt<? super T, ? extends N> select) {
    return this.<N, N>average(select, asNumber());
  }

  default <N extends Number> One<N> average(final Func<? super T, ? extends N> select) {
    nonNullable(select, "select");
    return this.average((index, it) -> select.apply(it));
  }

  default One<Double> average() {
    return average(identity(), asDouble());
  }
}

