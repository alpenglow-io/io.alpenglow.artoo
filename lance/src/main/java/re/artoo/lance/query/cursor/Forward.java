package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

import java.util.Arrays;

public final class Forward<T> implements Cursor<T> {
  private final T[] elements;
  private int traverse;
  private int reverse;
  public Forward(T[] elements) {
    this(0, elements.length, elements);
  }
  private Forward(int traverse, int reverse, T[] elements) {
    this.traverse = traverse;
    this.reverse = reverse;
    this.elements = elements;
  }

  @Override
  public <R> R tick(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return fetch.invoke(traverse, elements[traverse++]);
  }

  @Override
  public Probe<T> reverse() {
    return new Backward<>(elements);
  }

  @Override
  public boolean hasNext() {
    return traverse < elements.length;
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onArray().apply(Arrays.copyOf(elements, elements.length));
  }
}
