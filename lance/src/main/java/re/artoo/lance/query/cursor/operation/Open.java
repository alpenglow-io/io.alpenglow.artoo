package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;

public final class Open<ELEMENT> implements Cursor<ELEMENT> {
  private final ELEMENT[] elements;
  private int index;

  @SafeVarargs
  public Open(ELEMENT... elements) {
    this(elements, 0);
  }

  private Open(ELEMENT[] elements, int index) {
    this.elements = elements;
    this.index = index;
  }

  @Override
  public boolean canFetch() {
    return index < elements.length;
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> apply) throws Throwable {
    try {
      return apply.invoke(index, elements[index]);
    } finally {
      index++;
    }
  }
}
