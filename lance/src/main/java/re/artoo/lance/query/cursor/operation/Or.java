package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Or<ELEMENT>(Fetch<ELEMENT> fetch, Fetch<ELEMENT> otherwise) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return fetch.hasNext() || otherwise.hasNext();
  }

  @Override
  public Next<ELEMENT> fetch() {
    try {
      return fetch.hasNext()
        ? fetch.next()
        : otherwise.hasNext()
        ? otherwise.next()
        : FetchException.byThrowingCantFetchNextElement("or", "");
    } finally {
      // TODO: if (otherwise.hasNext()) otherwise.close();
    }
  }
}
