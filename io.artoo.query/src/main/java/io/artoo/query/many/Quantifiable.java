package io.artoo.query.many;

import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Quantify;


import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static io.artoo.type.Nullability.nonNullable;

// TODO: replace One<Boolean> with OneBoolean (internal primitive boolean and not boxed-boolean)
public interface Quantifiable<T> extends Queryable<T> {
  default <C> One<Boolean> allTypeOf(final Class<C> type) {
    return all((index, value) -> type.isInstance(value));
  }

  default One<Boolean> all(final Predicate<? super T> where) {
    nonNullable(where, "where");
    return all((index, value) -> where.test(value));
  }

  default One<Boolean> all(final BiPredicate<? super Integer, ? super T> where) {
    return new Quantify<>(this, (i, it) -> {}, false, nonNullable(where, "where"))::iterator;
  }

  default One<Boolean> any() { return this.any((i, t) -> true); }

  default One<Boolean> any(final BiPredicate<? super Integer, ? super T> where) {
    return new Quantify<>(this, (i, it) -> {}, true, nonNullable(where, "where"))::iterator;
  }
}

