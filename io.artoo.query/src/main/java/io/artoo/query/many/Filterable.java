package io.artoo.query.many;

import io.artoo.query.Many;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Where;


import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static io.artoo.type.Nullability.nonNullable;

public interface Filterable<T extends Record> extends Queryable<T> {
  default Many<T> where(final Predicate<? super T> where) {
    nonNullable(where, "where");
    return where((index, param) -> where.test(param));
  }

  default Many<T> where(final BiPredicate<? super Integer, ? super T> where) {
    return where(where, (i, it) -> it);
  }

  default <C extends Record> Many<C> ofType(final Class<? extends C> type) {
    nonNullable(type, "type");
    return where((index, it) -> type.isInstance(it), (index, it) -> type.cast(it));
  }

  default <R extends Record> Many<R> where(final BiPredicate<? super Integer, ? super T> where, final BiFunction<? super Integer, ? super T, ? extends R> select) {
    nonNullable(where, "where");
    nonNullable(select, "select");
    return new Where<T, R>(this, (i, it) -> {}, where, select)::iterator;
  }
}

