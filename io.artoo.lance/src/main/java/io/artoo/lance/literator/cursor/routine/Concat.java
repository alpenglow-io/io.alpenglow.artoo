package io.artoo.lance.literator.cursor.routine;

import io.artoo.lance.literator.cursor.Cursor;
import io.artoo.lance.literator.Literator;
import io.artoo.lance.func.Func;

import java.util.Arrays;
import java.util.Iterator;

import static java.lang.System.arraycopy;

sealed interface Concat<T> extends Routine<T, Cursor<T>> permits Concat.Array, Concat.Fetcher {
  final class Array<T> implements Concat<T> {
    private final T[] next;

    Array(final T[] next) {this.next = next;}

    @Override
    public final Func.Uni<T[], Cursor<T>> onArray() {
      return prev -> {
        final var length = prev.length + next.length;
        final var copied = Arrays.copyOf(prev, length);
        arraycopy(next, 0, copied, prev.length, next.length);
        return Cursor.open(copied);
      };
    }

    @Override
    public Func.Uni<Literator<T>, Cursor<T>> onLiterator() {
      return prev -> Cursor.link(prev, Cursor.open(next));
    }

    @Override
    public Func.Uni<Iterator<T>, Cursor<T>> onIterator() {
      return prev -> Cursor.link(Cursor.iteration(prev), Cursor.open(next));
    }
  }

  final class Fetcher<T> implements Concat<T> {
    private final Literator<T> next;

    Fetcher(final Literator<T> next) {this.next = next;}

    @Override
    public final Func.Uni<T[], Cursor<T>> onArray() {
      return prev -> Cursor.link(Cursor.open(prev), next);
    }

    @Override
    public Func.Uni<Literator<T>, Cursor<T>> onLiterator() {
      return prev -> Cursor.link(prev, next);
    }

    @Override
    public Func.Uni<Iterator<T>, Cursor<T>> onIterator() {
      return prev -> Cursor.link(Cursor.iteration(prev), next);
    }
  }
}
