package io.alpenglow.artoo.lance.literator.cursor.routine.concat;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.literator.Pointer;

import java.util.Iterator;

public final class Liter<T> implements Concat<T> {
  private final Pointer<T> next;

  Liter(final Pointer<T> pointer) {this.next = pointer;}

  @Override
  public final TryFunction1<T[], Cursor<T>> onArray() {
    return prev -> Cursor.link(Cursor.open(prev), next);
  }

  @Override
  public TryFunction1<Pointer<T>, Cursor<T>> onLiterator() {
    return prev -> Cursor.link(prev, next);
  }

  @Override
  public TryFunction1<Iterator<T>, Cursor<T>> onIterator() {
    return prev -> Cursor.link(Cursor.iteration(prev), next);
  }
}
