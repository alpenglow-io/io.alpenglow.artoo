package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;

public enum Empty implements Cursor<Object> {
  Default;
  @Override
  public boolean hasNext() {
    return false;
  }
  @Override
  public <NEXT> NEXT next(TryIntFunction1<? super Object, ? extends NEXT> then) {
    return FetchException.byThrowingCantFetchNextElement("empty", "");
  }
}
