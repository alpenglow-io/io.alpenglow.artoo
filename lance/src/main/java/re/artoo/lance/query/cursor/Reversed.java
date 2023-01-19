package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

import java.util.Arrays;

public final class Reversed<T> implements Cursor<T> {
  private final T[] elements;
  private int index;
  public Reversed(T[] elements) {
    this(elements.length, elements);
  }
  private Reversed(int length, T[] elements) {
    this.index = length;
    this.elements = elements;
  }

  @Override
  public <R> R traverse(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return fetch.invoke(index, elements[index--]);
  }

  @Override
  public Inquiry<T> reversal() {
    return new Open<>(elements);
  }

  @Override
  public boolean hasNext() {
    return index >= 0;
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onArray().apply(Arrays.copyOf(elements, elements.length));
  }
}
