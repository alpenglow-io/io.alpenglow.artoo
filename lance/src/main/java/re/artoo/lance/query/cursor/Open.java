package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
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
  public <R> R fetch(TryIntFunction1<? super T, ? extends R> detach) throws Throwable {
    return detach.invoke(index, elements[index++]);
  }

  @Override
  public boolean hasNext() {
    return index < elements.length;
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onArray().apply(Arrays.copyOf(elements, elements.length));
  }
}
