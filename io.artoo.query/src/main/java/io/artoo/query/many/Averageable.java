package io.artoo.query.many;

import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Average;
import io.artoo.value.Numeral;
import io.artoo.value.Single32;
import io.artoo.value.Single64;


import java.util.function.BiFunction;
import java.util.function.Function;

import static io.artoo.type.Nullability.nonNullable;
import static io.artoo.type.Numeric.asDouble;
import static io.artoo.type.Numeric.asNumber;
import static io.artoo.value.Numeral.asNumeral;
import static io.artoo.value.Numeral.asSingle64;
import static java.util.function.Function.identity;

public interface Averageable<T extends Record> extends Queryable<T> {
  default <V, N extends Number, R extends Record & Numeral<N, R>> One<R> average(final BiFunction<? super Integer, ? super T, ? extends V> select, final Function<? super V, ? extends R> asNumber) {
    return new Average<T, V, N, R>(this, (i, it) -> {}, (i, it) -> true, nonNullable(select, "select"), nonNullable(asNumber, "asNumber"))::iterator;
  }

  default <V, N extends Number, R extends Record & Numeral<N, R>> One<R> average(final Function<? super T, ? extends V> select, final Function<? super V, ? extends R> asNumeral) {
    nonNullable(select, "select");
    nonNullable(asNumeral, "asNumeral");
    return average((index, it) -> select.apply(it), asNumeral);
  }

  default <N extends Number, R extends Record & Numeral<N, R>> One<R> average(final BiFunction<? super Integer, ? super T, ? extends R> select) {
    return this.<R, N, R>average(select, asNumeral());
  }

  default <N extends Number, R extends Record & Numeral<N, R>> One<R> average(final Function<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return this.average((index, it) -> select.apply(it));
  }

  default One<Single64> average() {
    return this.<T, Double, Single64>average(identity(), asSingle64());
  }
}
