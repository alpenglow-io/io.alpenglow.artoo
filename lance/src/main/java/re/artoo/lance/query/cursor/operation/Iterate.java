package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;

import java.util.Iterator;

public final class Iterate<ELEMENT> extends Head<ELEMENT> implements Cursor<ELEMENT> {
  private final Iterator<ELEMENT> elements;

  public Iterate(Iterator<ELEMENT> elements) {
    super("iterable", "openable");
    this.elements = elements;
  }

  @Override
  public boolean hasElement() {
    if (!hasElement && (hasElement = elements.hasNext())) {
      this.set(index++, elements.next());
    }
    return hasElement;
  }
}
