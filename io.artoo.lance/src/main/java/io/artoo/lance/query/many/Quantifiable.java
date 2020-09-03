package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;

public interface Quantifiable<T> extends Queryable<T> {
  default <R> One<Boolean> all(final Class<R> type) {
    return all(type::isInstance);
  }

  default One<Boolean> all(final Pred.Uni<? super T> where) {
    return One.done(cursor().all(where));
  }

  default One<Boolean> any() { return this.any(t -> true); }

  default One<Boolean> any(final Pred.Uni<? super T> where) {
    return One.done(cursor().any(where)).or(false);
  }

  default One<Boolean> contains(final T element) {
    return One.done(cursor().contains(element));
  }

  default One<Boolean> notContains(final T element) {
    return One.done(cursor().notContains(element));
  }
}


