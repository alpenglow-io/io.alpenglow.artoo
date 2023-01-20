package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

import java.util.Arrays;

public final class Forth<T> implements Cursor<T> {
  private final T[] elements;
  private int traverse;
  private int reverse;
  public Forth(T[] elements) {
    this(0, elements.length, elements);
  }
  private Forth(int traverse, int reverse, T[] elements) {
    this.traverse = traverse;
    this.reverse = reverse;
    this.elements = elements;
  }

  @Override
  public <R> R traverse(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return fetch.invoke(traverse, elements[traverse++]);
  }

  @Override
  public Inquiry<T> reversal() {
    return new Reversed<>(elements);
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
