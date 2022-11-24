package lance.literator.cursor.routine.concat;

import lance.func.Func;
import lance.literator.Cursor;
import lance.literator.Literator;

import java.util.Iterator;

public final class Liter<T> implements Concat<T> {
  private final Literator<T> next;

  Liter(final Literator<T> next) {this.next = next;}

  @Override
  public final Func.TryFunction<T[], Cursor<T>> onArray() {
    return prev -> Cursor.link(Cursor.open(prev), next);
  }

  @Override
  public Func.TryFunction<Literator<T>, Cursor<T>> onLiterator() {
    return prev -> Cursor.link(prev, next);
  }

  @Override
  public Func.TryFunction<Iterator<T>, Cursor<T>> onIterator() {
    return prev -> Cursor.link(Cursor.iteration(prev), next);
  }
}
