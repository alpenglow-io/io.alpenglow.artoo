package io.artoo.lance.fetcher.routine;

import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.func.Func;

import java.util.Arrays;

import static java.lang.System.arraycopy;

sealed interface Concat<T> extends Routine<T, T> permits Concat.Array, Concat.Fetcher {
  final class Array<T> implements Concat<T> {
    private final T[] next;

    Array(final T[] next) {this.next = next;}

    @Override
    public final Func.Uni<T[], Cursor<T>> onArray() {
      return prev -> {
        final var length = prev.length + next.length;
        final var copied = Arrays.copyOf(prev, length);
        arraycopy(next, 0, copied, prev.length, length);
        return Cursor.open(copied);
      };
    }

    @Override
    public Func.Uni<io.artoo.lance.fetcher.Fetcher<T>, Cursor<T>> onFetcher() {
      return prev -> Cursor.link(prev, Cursor.open(next));
    }
  }

  final class Fetcher<T> implements Concat<T> {
    private final io.artoo.lance.fetcher.Fetcher<T> next;

    Fetcher(final io.artoo.lance.fetcher.Fetcher<T> next) {this.next = next;}

    @Override
    public final Func.Uni<T[], Cursor<T>> onArray() {
      return prev -> Cursor.link(Cursor.open(prev), next);
    }

    @Override
    public Func.Uni<io.artoo.lance.fetcher.Fetcher<T>, Cursor<T>> onFetcher() {
      return prev -> Cursor.link(prev, next);
    }
  }
}
