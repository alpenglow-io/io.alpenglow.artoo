package io.artoo.lance.literator.cursor.routine.concat;

import io.artoo.lance.func.Func;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.literator.Literator;

import java.util.Iterator;

public final class Liter<T> implements Concat<T> {
  private final Literator<T> next;

  Liter(final Literator<T> next) {this.next = next;}

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
