package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.Every;
import io.artoo.lance.query.internal.Some;
import io.artoo.lance.query.internal.None;

public interface Quantifiable<T> extends Queryable<T> {
  default <R> One<Boolean> every(final Class<R> type) {
    return every(type::isInstance);
  }

  default One<Boolean> every(final Pred.Uni<? super T> where) {
    return () -> cursor().map(new Every<>(where)).keepNull();
  }

  default <R> One<Boolean> none(final Class<R> type) {
    return none(type::isInstance);
  }

  default One<Boolean> none(final Pred.Uni<? super T> where) {
    return () -> cursor().map(new None<>(where)).keepNull();
  }

  default One<Boolean> some() { return this.some(t -> true); }

  default One<Boolean> some(final Pred.Uni<? super T> where) {
    return () -> cursor().map(new Some<>(where)).keepNull().or(() -> Cursor.open(false));
  }
}


