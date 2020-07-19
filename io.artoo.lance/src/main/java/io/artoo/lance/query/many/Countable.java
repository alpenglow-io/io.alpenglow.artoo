package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.query.operation.Count;

import static io.artoo.lance.type.Nullability.nonNullable;

interface Countable<T> extends Queryable<T> {
  default One<Integer> count() {
    return this.count(it -> true);
  }

  default One<Integer> count(final Pred.Uni<? super T> where) {
    final var w = nonNullable(where, "where");
    return One.done(() -> cursor().map(new Count<>(w))).or(() -> Cursor.just(0));
  }
}

