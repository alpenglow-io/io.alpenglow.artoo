package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Partitionable<T> extends Queryable<T> {
  default Many<T> skip(final int until) {
    return skipWhile((index, it) -> index < until);
  }

  default Many<T> skipWhile(final Pred.Uni<? super T> where) {
    return skipWhile((index, it) -> where.test(it));
  }

  default Many<T> skipWhile(final Pred.Bi<? super Integer, ? super T> where) {
    return Many.wany(cursor().skipWhile(where));
  }

  default Many<T> take(final int until) {
    return takeWhile((index, it) -> index < until);
  }

  default Many<T> takeWhile(final Pred.Uni<? super T> where) {
    nonNullable(where, "where");
    return takeWhile((index, param) -> where.test(param));
  }

  default Many<T> takeWhile(final Pred.Bi<? super Integer, ? super T> where) {
    return Many.wany(cursor().takeWhile(where));
  }
}
