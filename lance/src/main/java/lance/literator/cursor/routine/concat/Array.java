package lance.literator.cursor.routine.concat;

import lance.func.Func;
import lance.literator.Cursor;
import lance.literator.Literator;

import java.util.Arrays;
import java.util.Iterator;

import static java.lang.System.arraycopy;

public final class Array<T> implements Concat<T> {
  private final T[] next;

  Array(final T[] next) {this.next = next;}

  @Override
  public final Func.TryFunction<T[], Cursor<T>> onArray() {
    return prev -> {
      final var length = prev.length + next.length;
      final var copied = Arrays.copyOf(prev, length);
      arraycopy(next, 0, copied, prev.length, next.length);
      return Cursor.open(copied);
    };
  }

  @Override
  public Func.TryFunction<Literator<T>, Cursor<T>> onLiterator() {
    return prev -> Cursor.link(prev, Cursor.open(next));
  }

  @Override
  public Func.TryFunction<Iterator<T>, Cursor<T>> onIterator() {
    return prev -> Cursor.link(Cursor.iteration(prev), Cursor.open(next));
  }
}
