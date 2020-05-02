package io.artoo.query.many;

import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Average;


import java.util.function.BiFunction;
import java.util.function.Function;

import static io.artoo.type.Nullability.nonNullable;
import static io.artoo.type.Numeric.asDouble;
import static io.artoo.type.Numeric.asNumber;
import static java.util.function.Function.identity;

interface Averageable<T> extends Queryable<T> {
  default <V, N extends Number> One<N> average(final BiFunction<? super Integer, ? super T, ? extends V> select, final Function<? super V, ? extends N> asNumber) {
    return new Average<T, V, N>(this, (i, it) -> {}, (i, it) -> true, nonNullable(select, "select"), nonNullable(asNumber, "asNumber"))::iterator;
  }

  default <V, N extends Number> One<N> average(final Function<? super T, ? extends V> select, final Function<? super V, ? extends N> asNumber) {
    nonNullable(select, "select");
    nonNullable(asNumber, "asNumber");
    return average((index, it) -> select.apply(it), asNumber);
  }

  default <N extends Number> One<N> average(final BiFunction<? super Integer, ? super T, ? extends N> select) {
    return this.<N, N>average(select, asNumber());
  }

  default <N extends Number> One<N> average(final Function<? super T, ? extends N> select) {
    nonNullable(select, "select");
    return this.average((index, it) -> select.apply(it));
  }

  default One<Double> average() {
    return average(identity(), asDouble());
  }
}

