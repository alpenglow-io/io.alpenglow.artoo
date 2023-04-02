package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Append<ELEMENT>(Fetch<ELEMENT> head, Fetch<ELEMENT> tail) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return head.hasNext() || tail.hasNext();
  }

  @Override
  public Next<ELEMENT> next() {
    return head.hasNext()
      ? head.next()
      : tail.hasNext()
      ? tail.next()
      : FetchException.byThrowingCantFetchNextElement("append", "appendable");
  }
}
