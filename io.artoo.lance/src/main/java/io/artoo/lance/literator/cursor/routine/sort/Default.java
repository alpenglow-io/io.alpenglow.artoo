package io.artoo.lance.literator.cursor.routine.sort;

import io.artoo.lance.func.Func;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.literator.Literator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public final class Default<T> implements Sort<T> {
  @Override
  public final Func.Uni<T[], Cursor<T>> onArray() {
    return elements -> {
      Arrays.sort(elements);
      return Cursor.open(elements);
    };
  }

  @Override
  public Func.Uni<Literator<T>, Cursor<T>> onLiterator() {
    return this::asSorted;
  }

  @Override
  public Func.Uni<Iterator<T>, Cursor<T>> onIterator() {
    return this::asSorted;
  }

  @NotNull
  private Cursor<T> asSorted(final Iterator<T> iterator) {
    return
      Cursor.iteration(
        new SortedList<T>()
          .addAll(iterator)
          .iterator()
      );
  }

}
