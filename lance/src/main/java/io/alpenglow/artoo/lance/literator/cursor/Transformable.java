package io.alpenglow.artoo.lance.literator.cursor;

import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.literator.Pointer;
import io.alpenglow.artoo.lance.literator.cursor.routine.Routine;

public interface Transformable<T> extends Pointer<T> {
  default <R, C extends Cursor<R>> C to(Routine<T, C> routine) {
    return this.as(routine);
  }
  <R> R as(Routine<T, R> routine);
}

