package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.All;
import io.artoo.lance.query.internal.Any;
import io.artoo.lance.query.internal.None;

public interface Quantifiable<T> extends Queryable<T> {
  default <R> One<Boolean> all(final Class<R> type) {
    return all(type::isInstance);
  }

  default One<Boolean> all(final Pred.Uni<? super T> where) {
    return () -> cursor().map(new All<>(where)).walkDown();
  }

  default <R> One<Boolean> none(final Class<R> type) {
    return none(type::isInstance);
  }

  default One<Boolean> none(final Pred.Uni<? super T> where) {
    return () -> cursor().map(new None<>(where)).walkDown();
  }

  default One<Boolean> any() { return this.any(t -> true); }

  default One<Boolean> any(final Pred.Uni<? super T> where) {
    return () -> cursor().map(new Any<>(where)).walkDown().or(() -> Cursor.open(false));
  }
/*
  default One<Boolean> contains(final T element) {
    return One.done(cursor().contains(element));
  }

  default One<Boolean> notContains(final T element) {
    return One.done(cursor().notContains(element));
  }*/
}


