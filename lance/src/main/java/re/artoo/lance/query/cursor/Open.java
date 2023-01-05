package re.artoo.lance.query.cursor;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

import java.util.Arrays;

public final class Open<T> implements Cursor<T> {
  private final T[] elements;
  private int index = 0;
  public Open(final T[] elements) {
    this.elements = elements;
  }
  @Override
  public T fetch() {
    return elements[index++];
  }

  @Override
  public boolean hasNext() {
    var hasNext = index < elements.length;
    while (hasNext && elements[index] == null) {
      hasNext = ++index < elements.length;
    }
    return hasNext;
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onArray().apply(Arrays.copyOf(elements, elements.length));
  }
}
