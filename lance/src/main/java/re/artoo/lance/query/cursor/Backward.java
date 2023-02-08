package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;

public final class Backward<T> implements Cursor<T> {
  private final T[] elements;
  private int index;
  public Backward(T[] elements) {
    this(elements.length - 1, elements);
  }
  private Backward(int length, T[] elements) {
    this.index = length;
    this.elements = elements;
  }

  @Override
  public <R> R scroll(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return fetch.invoke(index, elements[index--]);
  }

  @Override
  public boolean hasNext() {
    return index >= 0;
  }

}
