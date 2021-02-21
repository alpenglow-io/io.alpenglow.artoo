package io.artoo.lance.literator.cursor.routine;

import io.artoo.lance.func.Func;
import io.artoo.lance.literator.Literator;

import java.util.Iterator;
import java.util.List;

public sealed interface Routine<T, R> permits Concat, Convert, Sort {
  static <T> Sort<T> orderByHashcode() { return new Sort.ByHashcode<>(); }
  static <T> Concat<T> concat(T[] elements) { return new Concat.Array<>(elements); }
  static <T> Concat<T> concat(Literator<T> elements) { return new Concat.Fetcher<>(elements); }

  static <T> Convert<T, List<T>> list() { return new Convert.Listable<>(); }

  static <T> Convert<T, T[]> array(final Class<T> type) {
    return new Convert.Arrayable<>(type);
  }

  static <R, T> Sort<T> orderBy(Func.Uni<? super T, ? extends R> field) {
    return new Sort.ByField<>(field);
  }

  Func.Uni<T[], R> onArray();
  Func.Uni<Literator<T>, R> onLiterator();
  Func.Uni<Iterator<T>, R> onIterator();

  @SuppressWarnings("unchecked")
  default Func.Uni<T, R> onPlain() {
    return it -> (R) it;
  }
}

