package io.alpenglow.artoo.lance.query.cursor.routine.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import static java.util.Collections.binarySearch;
import static java.util.Comparator.comparing;

final class SortedList<T> extends ArrayList<T> {
  private final Comparator<T> comparator;

  SortedList() {
    this(comparing(T::hashCode));
  }
  SortedList(final Comparator<T> comparator) {this.comparator = comparator;}

  public boolean add(T item) {
    if (item == null)
      return false;

    var index = binarySearch(this, item, comparator);
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
