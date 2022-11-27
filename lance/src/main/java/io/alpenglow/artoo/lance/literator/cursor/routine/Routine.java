package io.alpenglow.artoo.lance.literator.cursor.routine;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.literator.Literator;
import io.alpenglow.artoo.lance.literator.cursor.routine.concat.Concat;
import io.alpenglow.artoo.lance.literator.cursor.routine.convert.Convert;
import io.alpenglow.artoo.lance.literator.cursor.routine.join.Join;
import io.alpenglow.artoo.lance.literator.cursor.routine.sort.Sort;

import java.util.Iterator;
import java.util.List;

public sealed interface Routine<T, R> permits Join, Concat, Convert, Sort {

  static <T> Convert<T, List<T>> list() { return new Convert.Listable<>(); }

  static <T> Convert<T, T[]> array(final Class<T> type) {
    return new Convert.Arrayable<>(type);
  }

  TryFunction1<T[], R> onArray();
  TryFunction1<Literator<T>, R> onLiterator();
  TryFunction1<Iterator<T>, R> onIterator();

  @SuppressWarnings("unchecked")
  default TryFunction1<T, R> onSelf() {
    return it -> (R) it;
  }
}

