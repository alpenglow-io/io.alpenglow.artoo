package re.artoo.lance.value.steam;

import re.artoo.lance.value.Steam;

import java.util.Iterator;

public enum None implements Steam<Object> {
  Companion;

  @Override
  public Iterator<Object> iterator() {
    return N.Default;
  }

  private enum N implements Iterator<Object> {
    Default;

    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public Object next() {
      return null;
    }
  }
}
