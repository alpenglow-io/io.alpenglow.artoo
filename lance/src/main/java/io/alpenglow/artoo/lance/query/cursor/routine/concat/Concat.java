package io.alpenglow.artoo.lance.query.cursor.routine.concat;

import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.Repeatable;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

public sealed interface Concat<T> extends Routine<T, Cursor<T>> permits Array, Liter {
  @SafeVarargs
  static <T> Concat<T> array(T... elements) {
    return new Array<>(elements);
  }

  static <T> Concat<T> liter(Repeatable<T> repeatable) {
    return new Liter<>(repeatable);
  }

}
