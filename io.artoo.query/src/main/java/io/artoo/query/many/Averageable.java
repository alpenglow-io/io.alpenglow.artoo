package io.artoo.query.many;

import io.artoo.func.$2.ConsInt;
import io.artoo.func.$2.FuncInt;
import io.artoo.func.Func;
import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Average;

import static io.artoo.func.$2.FuncInt.identity;
import static io.artoo.func.$2.PredInt.tautology;
import static io.artoo.type.Nullability.nonNullable;
import static io.artoo.type.Numeric.asDouble;
import static io.artoo.type.Numeric.asNumber;

interface Averageable<T> extends Queryable<T> {
  default <V, N extends Number> One<N> average(final FuncInt<? super T, ? extends V> select, final io.artoo.func.Func<? super V, ? extends N> asNumber) {
    return new Average<T, V, N>(this, ConsInt.nothing(), tautology(), nonNullable(select, "select"), nonNullable(asNumber, "asNumber"))::iterator;
  }

  default <V, N extends Number> One<N> average(final Func<? super T, ? extends V> select, final io.artoo.func.Func<? super V, ? extends N> asNumber) {
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

