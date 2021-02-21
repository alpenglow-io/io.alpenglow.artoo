package io.artoo.lance.literator.cursor.routine;

import io.artoo.lance.literator.Literator;
import io.artoo.lance.func.Func;

import java.util.Iterator;
import java.util.List;

public sealed interface Routine<T, R> permits Concat, Convert, Curry, Sort {
  static <T> Sort<T> orderByHashcode() { return new Sort.Hashcode<>(); }
  static <T> Concat<T> concat(T[] elements) { return new Concat.Array<>(elements); }
  static <T> Concat<T> concat(Literator<T> elements) { return new Concat.Fetcher<>(elements); }
  static <T, R> Curry<T, R> curry(final Func.Uni<? super T, ? extends R> map) { return new Curry<>(map); }

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

