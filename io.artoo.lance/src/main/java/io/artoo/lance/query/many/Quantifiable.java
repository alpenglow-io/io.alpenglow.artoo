package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.oper.All;
import io.artoo.lance.query.oper.Any;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Quantifiable<T> extends Queryable<T> {
  default <R> One<Boolean> all(final Class<R> type) {
    return all(type::isInstance);
  }

  default One<Boolean> all(final Pred.Uni<? super T> where) {
    return () -> cursor().map(new All<>(where)).scroll();
  }

  default One<Boolean> any() { return this.any(t -> true); }

  default One<Boolean> any(final Pred.Uni<? super T> where) {
    return () -> cursor().map(new Any<>(where)).scroll().or(() -> Cursor.open(false));
  }
/*
  default One<Boolean> contains(final T element) {
    return One.done(cursor().contains(element));
  }

  default One<Boolean> notContains(final T element) {
    return One.done(cursor().notContains(element));
  }*/
}


