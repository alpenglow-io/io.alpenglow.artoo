package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

public record Or<ELEMENT>(Probe<ELEMENT> probe, Probe<ELEMENT> otherwise) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return probe.hasNext() || otherwise.hasNext();
  }

  @Override
  public Next<ELEMENT> fetch() {
    try {
      return probe.hasNext()
        ? probe.fetch()
        : otherwise.hasNext()
        ? otherwise.fetch()
        : FetchException.byThrowingCantFetchNextElement("or", "");
    } finally {
      // TODO: if (otherwise.hasNext()) otherwise.close();
    }
  }
}
