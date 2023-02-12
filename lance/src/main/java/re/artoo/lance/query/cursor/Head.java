package re.artoo.lance.query.cursor;

import re.artoo.lance.query.Cursor;

public final class Head<ELEMENT> implements Cursor<ELEMENT> {
  private final ELEMENT[] elements;
  private int index;

  public Head(ELEMENT[] elements) {
    this(0, elements);
  }
  private Head(int index, ELEMENT[] elements) {
    this.index = index;
    this.elements = elements;
  }

  @Override
  public ELEMENT tick() throws Throwable {
    return isTickable() ? elements[index++] : null;
  }

  @Override
  public Probe<ELEMENT> rewind() {
    return new Head<>(elements);
  }

  @Override
  public boolean isTickable() {
    return index < elements.length;
  }
}
