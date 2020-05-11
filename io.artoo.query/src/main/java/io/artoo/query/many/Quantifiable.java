package io.artoo.query.many;

import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Quantify;
import io.artoo.value.Bool;


import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static io.artoo.type.Nullability.nonNullable;

public interface Quantifiable<T extends Record> extends Queryable<T> {
  default <C> One<Bool> allTypeOf(final Class<C> type) {
    return all((index, value) -> type.isInstance(value));
  }

  default One<Bool> all(final Predicate<? super T> where) {
    nonNullable(where, "where");
    return all((index, value) -> where.test(value));
  }

  default One<Bool> all(final BiPredicate<? super Integer, ? super T> where) {
    return new Quantify<>(this, (i, it) -> {}, Bool.False, nonNullable(where, "where"))::iterator;
  }

  default One<Bool> any() { return this.any((i, t) -> true); }

  default One<Bool> any(final BiPredicate<? super Integer, ? super T> where) {
    return new Quantify<>(this, (i, it) -> {}, Bool.True, nonNullable(where, "where"))::iterator;
  }
}

