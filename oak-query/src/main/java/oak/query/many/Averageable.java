package oak.query.many;

import oak.func.$2.IntCons;
import oak.func.$2.IntFunc;
import oak.query.Queryable;
import oak.query.One;
import oak.query.many.internal.Average;

import static oak.func.$2.IntPred.tautology;
import static oak.func.$2.IntFunc.identity;
import static oak.type.Nullability.nonNullable;
import static oak.type.Numeric.asDouble;
import static oak.type.Numeric.asNumber;
import static oak.type.Numeric.divide;

interface Averageable<T> extends Queryable<T> {
  default <V, N extends Number> One<N> average(final IntFunc<? super T, ? extends V> select, final oak.func.Func<? super V, ? extends N> asNumber) {
    return new Average<T, V, N>(this, IntCons.nothing(), tautology(), nonNullable(select, "select"), nonNullable(asNumber, "asNumber"))::iterator;
  }

  default <V, N extends Number> One<N> average(final IntFunc<? super T, ? extends V> select) {
    return this.<V, N>average(select, asNumber());
  }

  default One<Double> average() {
    return average(identity(), asDouble());
  }
}

