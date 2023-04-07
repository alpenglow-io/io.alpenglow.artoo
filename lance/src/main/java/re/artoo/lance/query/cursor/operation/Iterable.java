package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;

import java.util.List;

public record Iterable<ELEMENT>(List<ELEMENT> elements, Index index) implements Cursor<ELEMENT> {
  public Iterable(List<ELEMENT> elements) {
    this(elements, new Index());
  }

  @Override
  public boolean hasNext() {
    return index.value <= elements.size();
  }

  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    return hasNext() ? then.apply(index.value, elements.get(index.value++)) : FetchException.byThrowingCantFetchNextElement("iterable", "fetchable");
  }

  private static class Index {
    private int value = 0;
  }
}
