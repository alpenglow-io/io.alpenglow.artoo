package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

public sealed interface Transformable<T> extends Source<T> permits Cursor, As {
  default <R, C extends Cursor<R>> C to(Routine<T, C> routine) {
    return this.as(routine);
  }
  <R> R as(Routine<T, R> routine);
}

