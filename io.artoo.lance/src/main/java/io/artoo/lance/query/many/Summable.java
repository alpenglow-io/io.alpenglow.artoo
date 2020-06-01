package io.artoo.lance.query.many;

import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.value.Numeral;

import java.util.function.Function;

import static io.artoo.lance.type.Nullability.nonNullable;

@SuppressWarnings("unchecked")
interface Summable<T extends Record> extends Queryable<T> {

  default <R extends Record & Numeral<R>> One<R> sum(final Function<? super T, ? extends Number> select) {
    final var sel = nonNullable(select, "select");
    return new Aggregate<T, R, R>(this, it -> {}, null, Numeral::isNumber, it -> Numeral.from(sel.apply(it)), (result, value) -> Numeral.add(result, value))::iterator;
  }

  default <R extends Record & Numeral<R>> One<R> sum() {
    return new Aggregate<T, R, R>(this, it -> {}, null, Numeral::isNumber, Numeral::from$, (result, value) -> Numeral.add(result, value))::iterator;
  }
}
