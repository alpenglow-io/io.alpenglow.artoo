package io.alpenglow.artoo.lance.query.cursor.routine.concat;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.Source;

import java.util.Arrays;
import java.util.Iterator;

import static java.lang.System.arraycopy;

public final class Array<T> implements Concat<T> {
  private final T[] next;
  Array(final T[] next) {this.next = next;}

  @Override
  public TryFunction1<T[], Cursor<T>> onArray() {
    return prev -> {
      final var length = prev.length + next.length;
      final var copied = Arrays.copyOf(prev, length);
      arraycopy(next, 0, copied, prev.length, next.length);
      return Cursor.open(copied);
    };
  }

  @Override
  public TryFunction1<Source<T>, Cursor<T>> onSource() {
    return prev -> Cursor.link(prev, Cursor.open(next));
  }

  @Override
  public TryFunction1<Iterator<T>, Cursor<T>> onIterator() {
    return prev -> Cursor.link(Cursor.iteration(prev), Cursor.open(next));
  }
}
