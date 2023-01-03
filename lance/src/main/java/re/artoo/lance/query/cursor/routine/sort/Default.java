package re.artoo.lance.query.cursor.routine.sort;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetcher;

import java.util.Arrays;
import java.util.Iterator;

public final class Default<T> implements Sort<T> {
  @Override
  public TryFunction1<T[], Cursor<T>> onArray() {
    return elements -> {
      Arrays.sort(elements);
      return Cursor.open(elements);
    };
  }

  @Override
  public TryFunction1<Fetcher<T>, Cursor<T>> onSource() {
    return this::asSorted;
  }

  @Override
  public TryFunction1<Iterator<T>, Cursor<T>> onIterator() {
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
