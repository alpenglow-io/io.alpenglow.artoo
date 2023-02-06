package re.artoo.lance.value.array;

import re.artoo.lance.value.Array;

import java.util.Iterator;
import java.util.NoSuchElementException;

public enum Empty implements Array<Object> {
  Default;

  @Override
  public Iterator<Object> iterator() {
    return Zero.Default;
  }

  private enum Zero implements Iterator<Object> {
    Default;

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
