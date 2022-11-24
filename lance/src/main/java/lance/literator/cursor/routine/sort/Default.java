package lance.literator.cursor.routine.sort;

import lance.func.Func;
import lance.literator.Cursor;
import lance.literator.Literator;

import java.util.Arrays;
import java.util.Iterator;

public final class Default<T> implements Sort<T> {
  @Override
  public Func.TryFunction<T[], Cursor<T>> onArray() {
    return elements -> {
      Arrays.sort(elements);
      return Cursor.open(elements);
    };
  }

  @Override
  public Func.TryFunction<Literator<T>, Cursor<T>> onLiterator() {
    return this::asSorted;
  }

  @Override
  public Func.TryFunction<Iterator<T>, Cursor<T>> onIterator() {
    return this::asSorted;
  }

  private Cursor<T> asSorted(final Iterator<T> iterator) {
    return
      Cursor.iteration(
        new SortedList<T>()
          .addAll(iterator)
          .iterator()
      );
  }

}
