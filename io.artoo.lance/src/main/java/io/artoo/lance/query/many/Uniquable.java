package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.operation.At;
import io.artoo.lance.query.operation.First;
import io.artoo.lance.query.operation.Last;
import io.artoo.lance.query.operation.Single;

import static io.artoo.lance.type.Nullability.nonNullable;
import static io.artoo.lance.type.Nullability.nullable;

public interface Uniquable<T> extends Queryable<T> {
  default One<T> at(final int index) {
    return One.done(cursor().at(index));
  }

  default One<T> first() {
    return first(it -> true);
  }

  default One<T> first(final Pred.Uni<? super T> where) {
    return One.done(cursor().first(where));
  }

  default One<T> last() {
    return last(it -> true);
  }

  default One<T> last(final Pred.Uni<? super T> where) {
    return One.done(cursor().last(where));
  }

  default One<T> single() {
    return single(it -> true);
  }

  default One<T> single(final Pred.Uni<? super T> where) {
    return One.done(cursor().single(where));
  }
}

