package io.artoo.lance.fetcher.routine;

import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.fetcher.Fetcher;
import io.artoo.lance.func.Func;

import java.util.Arrays;

final class Sort<T> implements Routine<T, T> {
  @Override
  public final Func.Uni<T[], Cursor<T>> onArray() {
    return elements -> {
      Arrays.sort(elements);
      return Cursor.open(elements);
    };
  }

  @Override
  public Func.Uni<Fetcher<T>, Cursor<T>> onFetcher() {
    return it -> Cursor.nothing();
  }
}
