package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.func.At;
import io.artoo.lance.query.func.First;
import io.artoo.lance.query.func.Last;
import io.artoo.lance.query.func.Single;

public interface Uniquable<T> extends Queryable<T> {
  default One<T> at(final int index) {
    return () -> cursor().map(new At<>(index)).keepNull();
  }

  default One<T> first() {
    return first(it -> true);
  }

  default One<T> first(final Pred.MaybePredicate<? super T> where) {
    return () -> cursor().map(new First<>(where)).skipNull();
  }

  default One<T> last() {
    return last(it -> true);
  }

  default One<T> last(final Pred.MaybePredicate<? super T> where) {
    return () -> cursor().map(new Last<>(where)).skipNull();
  }

  default One<T> single() {
    return single(it -> true);
  }

  default One<T> single(final Pred.MaybePredicate<? super T> where) {
    return () -> cursor().map(new Single<>(where)).keepNull();
  }
}

