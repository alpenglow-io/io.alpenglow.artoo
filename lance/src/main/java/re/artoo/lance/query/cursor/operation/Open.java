package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;

public record Open<ELEMENT>(ELEMENT[] elements, Index index) implements Cursor<ELEMENT> {
  @SafeVarargs
  public Open(ELEMENT... elements) {
    this(elements, new Index());
  }
  @Override
  public boolean hasNext() {
    return index.value < elements.length;
  }
  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    return hasNext() ? then.apply(index.value, elements[index.value++]) : FetchException.byThrowingCantFetchNextElement("open", "fetchable");
  }

  private static class Index {
    private int value = 0;
  }
}
