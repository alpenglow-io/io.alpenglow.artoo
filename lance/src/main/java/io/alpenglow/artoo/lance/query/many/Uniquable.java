package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.At;
import io.alpenglow.artoo.lance.query.func.First;
import io.alpenglow.artoo.lance.query.func.Last;
import io.alpenglow.artoo.lance.query.func.Single;

public interface Uniquable<T> extends Queryable<T> {
  default One<T> at(final int index) {
    return () -> cursor().map(new At<>(index)).keepNull();
  }

  default One<T> first() {
    return first(it -> true);
  }

  default One<T> first(final TryPredicate1<? super T> where) {
    return () -> cursor().map(new First<>(where)).skipNull();
  }

  default One<T> last() {
    return last(it -> true);
  }

  default One<T> last(final TryPredicate1<? super T> where) {
    return () -> cursor().map(new Last<>(where)).skipNull();
  }

  default One<T> single() {
    return single(it -> true);
  }

  default One<T> single(final TryPredicate1<? super T> where) {
    return () -> cursor().map(new Single<>(where)).keepNull();
  }
}

