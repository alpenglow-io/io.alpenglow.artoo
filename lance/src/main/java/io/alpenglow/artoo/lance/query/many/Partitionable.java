package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.func.TryBiPredicate;
import io.alpenglow.artoo.lance.func.TryPredicate;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.Skip;
import io.alpenglow.artoo.lance.query.func.Take;

public interface Partitionable<T> extends Queryable<T> {
  default Many<T> skip(final int until) {
    return skipWhile((index, it) -> index < until);
  }

  default Many<T> skipWhile(final TryPredicate<? super T> where) {
    return skipWhile((index, it) -> where.test(it));
  }

  default Many<T> skipWhile(final TryBiPredicate<? super Integer, ? super T> where) {
    return () -> cursor().map(new Skip<T, T>(where));
  }

  default Many<T> take(final int until) {
    return takeWhile((index, it) -> index < until);
  }

  default Many<T> takeWhile(final TryPredicate<? super T> where) {
    return takeWhile((index, param) -> where.test(param));
  }

  default Many<T> takeWhile(final TryBiPredicate<? super Integer, ? super T> where) {
    return () -> cursor().map(new Take<T, T>(where));
  }
}
