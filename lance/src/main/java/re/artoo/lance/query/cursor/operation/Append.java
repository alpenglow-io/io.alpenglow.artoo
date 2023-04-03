package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

public record Append<ELEMENT>(Probe<ELEMENT> head, Probe<ELEMENT> tail) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return head.hasNext() || tail.hasNext();
  }

  @Override
  public Next<ELEMENT> fetch() {
    return head.hasNext()
      ? head.fetch()
      : tail.hasNext()
      ? tail.fetch()
      : FetchException.byThrowingCantFetchNextElement("append", "appendable");
  }
}
