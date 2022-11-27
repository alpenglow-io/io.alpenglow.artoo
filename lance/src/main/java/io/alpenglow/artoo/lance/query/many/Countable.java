package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.func.TryPredicate1;
import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.query.One;
import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.query.func.Count;

public interface Countable<T> extends Queryable<T> {
  default One<Integer> count() {
    return count(it -> true);
  }

  default One<Integer> count(final TryPredicate1<? super T> where) {
    return () -> cursor().map(new Count<>(where)).or(() -> Cursor.open(0)).keepNull();
  }
}

