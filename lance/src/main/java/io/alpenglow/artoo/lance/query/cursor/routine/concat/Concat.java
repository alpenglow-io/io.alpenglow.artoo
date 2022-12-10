package io.alpenglow.artoo.lance.query.cursor.routine.concat;

import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.Fetcher;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

public sealed interface Concat<T> extends Routine<T, Cursor<T>> permits Array, Liter {
  @SafeVarargs
  static <T> Concat<T> array(T... elements) {
    return new Array<>(elements);
  }

  static <T> Concat<T> liter(Fetcher<T> fetcher) {
    return new Liter<>(fetcher);
  }

}
