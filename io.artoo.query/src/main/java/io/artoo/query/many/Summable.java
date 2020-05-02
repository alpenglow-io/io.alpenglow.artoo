package io.artoo.query.many;


import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Aggregate;
import io.artoo.type.AsFloat;
import io.artoo.type.Numeric;


import java.util.function.Function;

import static io.artoo.type.Nullability.nonNullable;
import static io.artoo.type.Numeric.asNumber;
import static java.util.function.Function.identity;

interface Summable<T extends Record> extends Queryable<T> {
  default <V, N extends Number> One<N> sum(final Function<? super T, ? extends V> select, final Function<? super V, ? extends N> asNumber) {
    nonNullable(select, "select");
    nonNullable(asNumber, "asNumber");
    return new Aggregate<T, N, N>(this, it -> {}, null, it -> true, value -> select.andThen(asNumber).apply(value), Numeric::sum)::iterator;
  }

  default <V, N extends Number> One<N> sum(final Function<? super T, ? extends V> select) {
    return this.<V, N>sum(select, asNumber());
  }

  default <N extends Number> One<N> sum() {
    return this.<T, N>sum(identity(), asNumber());
  }
}
