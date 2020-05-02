package io.artoo.query.one;

import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.one.impl.Where;

import java.util.function.Predicate;

import static io.artoo.type.Nullability.nonNullable;
import static java.util.function.Function.identity;

public interface Filterable<T extends Record> extends Queryable<T> {
  default One<T> where(final Predicate<? super T> where) {
    return new Where<>(this, nonNullable(where, "where"), identity())::iterator;
  }

  default <R extends Record> One<R> ofType(final Class<R> type) {
    nonNullable(type, "type");
    return new Where<>(this, type::isInstance, type::cast)::iterator;
  }
}
