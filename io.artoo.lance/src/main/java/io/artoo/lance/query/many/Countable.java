package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.internal.Count;

public interface Countable<T> extends Queryable<T> {
  default One<Integer> count() {
    return count(it -> true);
  }

  default One<Integer> count(final Pred.Uni<? super T> where) {
    return () -> cursor().map(new Count<>(where)).or(() -> Cursor.open(0)).scroll();
  }
}

