package io.alpenglow.artoo.lance.literator.cursor.routine.sort;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.literator.Literator;

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
  public TryFunction1<Literator<T>, Cursor<T>> onLiterator() {
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
