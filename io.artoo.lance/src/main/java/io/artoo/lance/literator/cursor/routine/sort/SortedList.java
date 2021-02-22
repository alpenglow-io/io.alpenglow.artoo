package io.artoo.lance.literator.cursor.routine.sort;

import java.util.ArrayList;
import java.util.Iterator;

import static java.util.Collections.binarySearch;
import static java.util.Comparator.comparing;

final class SortedList<T> extends ArrayList<T> {
  public boolean add(T item) {
    if (item == null)
      return false;

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
