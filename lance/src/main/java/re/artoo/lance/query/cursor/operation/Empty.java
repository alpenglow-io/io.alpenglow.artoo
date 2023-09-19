package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public enum Empty implements Cursor<Object> {
  Default;

  @Override
  public boolean canFetch() {
    return false;
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super Object, ? extends NEXT> then) {
    return Fetchable.Exception.of("empty", "");
  }

}
