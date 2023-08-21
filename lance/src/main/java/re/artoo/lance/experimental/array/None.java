package re.artoo.lance.experimental.array;

import re.artoo.lance.value.Array;

import java.util.Iterator;
import java.util.NoSuchElementException;

public enum None implements Array<Object> {
  Companion;

  @SafeVarargs
  public final <T> T[] elements(T... elements) {
    return elements;
  }

  @Override
  public Iterator<Object> iterator() {
    return Zero.Companion;
  }

  @Override
  public String toString() {
    return "None{}";
  }

  private enum Zero implements Iterator<Object> {
    Companion;

    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public Object next() {
      throw new NoSuchElementException();
    }
  }
}
