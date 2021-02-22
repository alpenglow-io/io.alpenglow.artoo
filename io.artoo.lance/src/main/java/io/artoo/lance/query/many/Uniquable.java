package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.impl.At;
import io.artoo.lance.query.impl.First;
import io.artoo.lance.query.impl.Last;
import io.artoo.lance.query.impl.Single;

public interface Uniquable<T> extends Queryable<T> {
  default One<T> at(final int index) {
    return () -> cursor().map(new At<>(index)).scroll();
  }

  default One<T> first() {
    return first(it -> true);
  }

  default One<T> first(final Pred.Uni<? super T> where) {
    return () -> cursor().map(new First<>(where)).scroll();
  }

  default One<T> last() {
    return last(it -> true);
  }

  default One<T> last(final Pred.Uni<? super T> where) {
    return () -> cursor().map(new Last<>(where)).scroll();
  }

  default One<T> single() {
    return single(it -> true);
  }

  default One<T> single(final Pred.Uni<? super T> where) {
    return () -> cursor().map(new Single<>(where)).scroll();
  }
}

