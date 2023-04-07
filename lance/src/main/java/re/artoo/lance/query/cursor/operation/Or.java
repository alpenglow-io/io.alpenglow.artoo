package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Or<ELEMENT>(Fetch<ELEMENT> fetch, Fetch<ELEMENT> otherwise) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return fetch.hasNext() || otherwise.hasNext();
  }

  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) {
    try {
      return hasNext() ? fetch.next(then) : otherwise.hasNext() ? otherwise.next(then) : FetchException.byThrowingCantFetchNextElement("or", "elseable");
    } finally {
      // TODO: if (otherwise.hasNext()) otherwise.close();
    }
  }
}
