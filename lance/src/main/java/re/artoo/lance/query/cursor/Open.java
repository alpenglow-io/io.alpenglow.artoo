package re.artoo.lance.query.cursor;

import re.artoo.lance.query.Cursor;

public final class Open<ELEMENT> implements Cursor<ELEMENT> {
  private final ELEMENT[] elements;
  private int index;

  public Open(ELEMENT[] elements) {
    this(-1, elements);
  }
  private Open(int index, ELEMENT[] elements) {
    this.index = index;
    this.elements = elements;
  }

  @Override
  public ELEMENT tick() throws Throwable {
    return isTickable() ? elements[index] : null;
  }

  @Override
  public Probe<ELEMENT> rewind() {
    return new Open<>(elements);
  }

  @Override
  public boolean isTickable() {
    boolean tickable = elements.length > 0 && elements[index + 1] != null;
    while (index < elements.length && !tickable) ++index;
    return tickable;
  }
}
