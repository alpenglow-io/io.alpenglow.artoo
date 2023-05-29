package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public enum Empty implements Cursor<Object> {
  Default;

  @Override
  public boolean hasElement() {
    return false;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super Object, ? extends NEXT> then) {
    return Fetch.Exception.of("empty", "");
  }

}
