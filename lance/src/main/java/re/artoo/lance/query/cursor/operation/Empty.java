package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;

public enum Empty implements Cursor<Object> {
  Default;
  @Override
  public Next<Object> fetch() {
    return FetchException.byThrowingCantFetchNextElement("empty", "empty");
  }

  @Override
  public boolean hasNext() {
    return false;
  }
}
