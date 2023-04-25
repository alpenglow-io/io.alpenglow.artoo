package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public record Iterable<ELEMENT>(Iterator<ELEMENT> elements, Index index) implements Cursor<ELEMENT> {
  public Iterable(Iterator<ELEMENT> elements) {
    this(elements, new Index());
  }

  @Override
  public boolean hasElement() {
    return elements.hasNext();
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    return hasElement() ? then.invoke(index.value++, elements.next()) : FetchException.byThrowingCantFetchNextElement("iterable", "fetchable");
  }

  private static class Index {
    private int value = 0;
  }
}
