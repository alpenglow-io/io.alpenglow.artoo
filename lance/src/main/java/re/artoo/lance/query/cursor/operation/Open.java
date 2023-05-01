package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;

public final class Open<ELEMENT> extends Head<ELEMENT> implements Cursor<ELEMENT> {
  private final ELEMENT[] elements;
  @SafeVarargs
  public Open(ELEMENT... elements) {
    super("open", "openable");
    this.elements = elements;
  }
  @Override
  public boolean hasElement() {
    if (!hasElement && (hasElement = index < elements.length)) {
      element = elements[index];
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> apply) throws Throwable {
    try {
      return super.element(apply);
    } finally {
      index++;
    }
  }
}
