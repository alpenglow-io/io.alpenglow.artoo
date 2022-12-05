package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

import java.util.Arrays;

public final class Open<T> implements Cursor<T> {
  private final Index index = Index.incremental();
  private final T[] elements;

  public Open(final T[] elements) {this.elements = elements;}

  @Override
  public T fetch() {
    try {
      return elements[index.value()];
    } finally {
      index.inc();
    }
  }

  @Override
  public boolean hasNext() {
    var hasNext = index.value() < elements.length;
    while (hasNext && elements[index.value()] == null) {
      index.inc();
      hasNext = index.value() < elements.length;
    }
    return hasNext;
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onArray().apply(Arrays.copyOf(elements, elements.length));
  }
}
