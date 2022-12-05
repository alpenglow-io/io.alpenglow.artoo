package io.alpenglow.artoo.lance.query.cursor.routine.concat;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.Source;

import java.util.Iterator;

public final class Liter<T> implements Concat<T> {
  private final Source<T> next;

  Liter(final Source<T> source) {this.next = source;}

  @Override
  public final TryFunction1<T[], Cursor<T>> onArray() {
    return prev -> Cursor.link(Cursor.open(prev), next);
  }

  @Override
  public TryFunction1<Source<T>, Cursor<T>> onSource() {
    return prev -> Cursor.link(prev, next);
  }

  @Override
  public TryFunction1<Iterator<T>, Cursor<T>> onIterator() {
    return prev -> Cursor.link(Cursor.iteration(prev), next);
  }
}
