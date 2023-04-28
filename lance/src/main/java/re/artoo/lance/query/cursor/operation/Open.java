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
    if (!hasElement) {
      //noinspection AssignmentUsedAsCondition
      if (hasElement = ++index < elements.length) set(index, elements[index]);
    }
    return hasElement;
  }
}
