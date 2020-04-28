package io.artoo.query.one;

import io.artoo.func.Func;
import io.artoo.func.Pred;
import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.one.impl.Where;

import static io.artoo.type.Nullability.nonNullable;

public interface Filterable<T> extends Queryable<T> {
  default One<T> where(final Pred<? super T> where) {
    return new Where<>(this, nonNullable(where, "where"), Func.identity())::iterator;
  }

  default <R> One<R> ofType(final Class<R> type) {
    nonNullable(type, "type");
    return new Where<>(this, type::isInstance, type::cast)::iterator;
  }
}
