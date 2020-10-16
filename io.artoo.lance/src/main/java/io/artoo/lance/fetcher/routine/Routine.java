package io.artoo.lance.fetcher.routine;

import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.fetcher.Fetcher;
import io.artoo.lance.func.Func;

import java.util.Arrays;

import static java.lang.System.arraycopy;

public sealed interface Routine<T, R> permits Sort, Concat {
  static <T> Sort<T> sort() { return new Sort<>(); }
  static <T> Concat<T> concat(T[] elements) { return new Concat.Array<>(elements); }
  static <T> Concat<T> concat(Fetcher<T> elements) { return new Concat.Fetcher<>(elements); }

  Func.Uni<T[], Cursor<R>> onArray();
  Func.Uni<Fetcher<T>, Cursor<R>> onFetcher();
}

