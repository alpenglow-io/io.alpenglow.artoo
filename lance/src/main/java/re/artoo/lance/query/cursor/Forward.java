package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;

public final class Forward<T> implements Cursor<T> {
  private final T[] elements;
  private int index;

  public Forward(T[] elements) {
    this(0, elements);
  }
  private Forward(int index, T[] elements) {
    this.index = index;
    this.elements = elements;
  }

  @Override
  public <R> R tick(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return fetch.invoke(index, elements[index++]);
  }

  @Override
  public Probe<T> reverse() {
    return new Backward<>(elements);
  }

  @Override
  public boolean hasNext() {
    return index < elements.length;
  }

}
