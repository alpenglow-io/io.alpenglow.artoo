package io.alpenglow.artoo.lance.literator.cursor.routine.concat;

import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.literator.Pointer;
import io.alpenglow.artoo.lance.literator.cursor.routine.Routine;

public sealed interface Concat<T> extends Routine<T, Cursor<T>> permits Array, Liter {
  @SafeVarargs
  static <T> Concat<T> array(T... elements) {
    return new Array<>(elements);
  }

  static <T> Concat<T> liter(Pointer<T> pointer) {
    return new Liter<>(pointer);
  }

}
