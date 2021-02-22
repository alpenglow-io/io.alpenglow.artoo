package io.artoo.lance.literator.cursor.routine;

import io.artoo.lance.func.Func;
import io.artoo.lance.literator.Literator;
import io.artoo.lance.literator.cursor.routine.concat.Concat;
import io.artoo.lance.literator.cursor.routine.convert.Convert;
import io.artoo.lance.literator.cursor.routine.sort.Sort;

import java.util.Iterator;
import java.util.List;

public sealed interface Routine<T, R> permits Concat, Convert, Sort {

  static <T> Convert<T, List<T>> list() { return new Convert.Listable<>(); }

  static <T> Convert<T, T[]> array(final Class<T> type) {
    return new Convert.Arrayable<>(type);
  }

  Func.Uni<T[], R> onArray();
  Func.Uni<Literator<T>, R> onLiterator();
  Func.Uni<Iterator<T>, R> onIterator();

  @SuppressWarnings("unchecked")
  default Func.Uni<T, R> onPlain() {
    return it -> (R) it;
  }
}

