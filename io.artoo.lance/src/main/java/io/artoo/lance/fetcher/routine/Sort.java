package io.artoo.lance.fetcher.routine;

import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.fetcher.Fetcher;
import io.artoo.lance.func.Func;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.binarySearch;
import static java.util.Comparator.comparing;

final class Sort<T> implements Routine<T, T> {
  @Override
  public final Func.Uni<T[], Cursor<T>> onArray() {
    return elements -> {
      Arrays.sort(elements);
      return Cursor.open(elements);
    };
  }

  @Override
  public Func.Uni<Fetcher<T>, Cursor<T>> onFetcher() {
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

  private static final class SortedList<T> extends ArrayList<T> {
    public boolean add(T item) {
      if (item == null) return false;

      var index = binarySearch(this, item, comparing(T::hashCode));
      if (index < 0)
        index = ~index;
      super.add(index, item);

      return true;
    }

    public Iterable<T> addAll(final Iterator<T> iterator) {
      iterator.forEachRemaining(this::add);
      return this;
    }
  }
}
