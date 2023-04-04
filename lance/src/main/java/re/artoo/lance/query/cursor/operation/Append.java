package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Append<ELEMENT>(Fetch<ELEMENT> head, Fetch<ELEMENT> tail) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return head.hasNext() || tail.hasNext();
  }

  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    return head.hasNext()
      ? head.next(then)
      : tail.hasNext()
      ? tail.next(then)
      : FetchException.byThrowingCantFetchNextElement("append", "appendable");
  }
}
