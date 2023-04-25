package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Append<ELEMENT>(Fetch<ELEMENT> head, Fetch<ELEMENT> tail) implements Cursor<ELEMENT> {
  @Override
  public boolean hasElement() throws Throwable {
    return head.hasElement() || tail.hasElement();
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    return head.hasElement()
      ? head.element(then)
      : tail.hasElement()
      ? tail.element(then)
      : FetchException.byThrowingCantFetchNextElement("append", "appendable");
  }
}
