package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Pred;
import io.artoo.lance.next.Cursor;

public interface Filterable<T> extends Projectable<T> {
  default Cursor<T> where(Pred.Uni<? super T> pred) {
    return select(it -> pred.tryTest(it) ? it : null);
  }

  default Cursor<T> where(Pred.Bi<? super Integer, ? super T> pred) {
    return select((index, it) -> pred.tryTest(index, it) ? it : null);
  }

  default <R> Cursor<R> ofType(final Class<? extends R> type) {
    return select(it -> type.isInstance(it) ? type.cast(it) : null);
  }
}
