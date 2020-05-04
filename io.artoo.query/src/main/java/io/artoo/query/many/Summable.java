package io.artoo.query.many;

import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Aggregate;
import io.artoo.type.Numeric;
import io.artoo.value.Numeral;

import java.util.function.Function;

import static io.artoo.type.Nullability.nonNullable;
import static java.util.function.Function.identity;

interface Summable<T extends Record> extends Queryable<T> {

  default <V, N extends Number, R extends Record & Numeral<N, R>> One<R> sum(
    final Function<? super T, ? extends V> select,
    final Function<? super V, ? extends R> asNumber
  ) {
    nonNullable(select, "select");
    nonNullable(asNumber, "asNumber");
    return new Aggregate<T, R, R>(
      this,
      it -> {},
      null,
      it -> true,
      value -> select.andThen(asNumber).apply(value),
      (result, value) -> Numeral.add(result, value))::iterator;
  }

  default <V, N extends Number, R extends Record & Numeral<N, R>> One<R> sum(
    final Function<? super T, ? extends V> select
  ) {
    return this.sum(select, Numeral.<V, N, R>asNumber());
  }

  default <N extends Number, R extends Record & Numeral<N, R>> One<R> sum() {
    return this.sum(identity(), Numeral.<T, N, R>asNumber());
  }
}
