package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.query.closure.Every;
import io.alpenglow.artoo.lance.query.closure.None;
import io.alpenglow.artoo.lance.query.closure.Some;

public interface Quantifiable<T> extends Queryable<T> {
  default <R> One<Boolean> every(final Class<R> type) {
    return every(type::isInstance);
  }

  default One<Boolean> every(final TryPredicate1<? super T> where) {
    return () -> cursor().map(new Every<>(where)).keepNull();
  }

  default <R> One<Boolean> none(final Class<R> type) {
    return none(type::isInstance);
  }

  default One<Boolean> none(final TryPredicate1<? super T> where) {
    return () -> cursor().map(new None<>(where)).keepNull();
  }

  default One<Boolean> some() { return this.some(t -> true); }

  default One<Boolean> some(final TryPredicate1<? super T> where) {
    return () -> cursor().map(new Some<>(where)).keepNull().or(() -> Cursor.open(false));
  }
}


