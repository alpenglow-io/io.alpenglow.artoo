package io.artoo.lance.fetcher.routine;

import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.fetcher.Fetcher;
import io.artoo.lance.func.Func;

public sealed interface Routine<T, R> permits Sort, Concat, Curry {
  static <T> Sort<T> sort() { return new Sort<>(); }
  static <T> Concat<T> concat(T[] elements) { return new Concat.Array<>(elements); }
  static <T> Concat<T> concat(Fetcher<T> elements) { return new Concat.Fetcher<>(elements); }
  static <T, R> Curry<T, R> curry(final Func.Uni<? super T, ? extends R> map) {
    return new Curry<>(map);
  }

  Func.Uni<T[], Cursor<R>> onArray();
  Func.Uni<Fetcher<T>, Cursor<R>> onFetcher();

  @SuppressWarnings("unchecked")
  default Func.Uni<T, Cursor<R>> onPlain() {
    return it -> Cursor.maybe((R) it);
  }
}

